package io.versailles.application.component.middleware

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props, Terminated}
import akka.io.Tcp
import akka.io.Tcp._
import akka.util.ByteString
import io.netty.buffer.{ByteBuf, ByteBufAllocator}
import io.versailles.application.component.middleware.Channel._
import io.versailles.application.component.middleware.MessageDecoder.Result.{MessageOutput, RequireMoreBytes}
import io.versailles.application.component.middleware.encoding.buffer._

import scala.annotation.tailrec

object Channel {
  case class SetHandler(handlerProps: Props)

  case class SetDecoder(decoder: MessageDecoder)
  case class SetEncoder[A](encoder: MessageEncoder[A])

  case class Send(message: Any)
  case object Flush

  case object Terminate

  def props(connection: ActorRef, address: InetSocketAddress, bufferAllocator: ByteBufAllocator) =
    Props(new Channel(connection, address, bufferAllocator))
}

/**
  * A bi-directional communication channel wrapping a socket connection
  * [[ActorRef]] to publish write events to.
  * @author Sino
  */
final class Channel(connection: ActorRef, address: InetSocketAddress, bufferAllocator: ByteBufAllocator) extends Actor with ActorLogging {
  /** The byte streams incoming data and outgoing data are first written to. */
  val upstream = bufferAllocator.buffer(512)
  val downstream = bufferAllocator.buffer(1024)

  /** Does the decision making for received messages. */
  var handlerOpt: Option[ActorRef] = None

  /** Decode and encode messages. */
  var decoderOpt: Option[MessageDecoder] = None
  var encoderOpt: Option[MessageEncoder[_]] = None

  /** Data that is pending for flushing due to congestion. */
  var currentlyFlushing: Option[ByteString] = None
  var pendingFlushes = Seq.empty[ByteString]

  override def preStart(): Unit = {
    // signs a death pact with the connection
    context.watch(connection)
  }

  override def postStop(): Unit = {
    upstream.release()
    downstream.release()
  }

  override def receive = {
    case Received(data) =>
      upstream.writeBytes(data.asByteBuffer)

      decodeUntilInsufficientBytes(upstream)
        .foreach { command =>
          handlerOpt.foreach { handler =>
            handler ! command
          }
        }

      // prevents buffer overflow from constant pile-up by discarding all the bytes
      // that have been read and shifting the remaining bytes to the front of the buffer
      upstream.discardReadBytes()

    case SetHandler(handlerProps) =>
      // kill off the previous handler
      handlerOpt.foreach(_ ! PoisonPill)

      // and set the next one in charge
      handlerOpt = Some(context.actorOf(handlerProps))

    case SetDecoder(decoder) =>
      decoderOpt = Some(decoder)

    case SetEncoder(encoder) =>
      encoderOpt = Some(encoder)

    case Send(event) =>
      encoderOpt
        .map(_.asInstanceOf[MessageEncoder[Any]])
        .foreach(_.encode(event, downstream))

    case Flush =>
      val bytesWritten = downstream.toByteString
      if (currentlyFlushing.isDefined) {
        pendingFlushes :+= bytesWritten
      } else {
        connection ! Write(bytesWritten, FlushAck)

        currentlyFlushing = Some(bytesWritten)
      }

      downstream.clear()

    case FlushAck =>
      currentlyFlushing = None
      if (pendingFlushes.nonEmpty) {
        connection ! Write(pendingFlushes.head, FlushAck)

        currentlyFlushing = Some(pendingFlushes.head)
        pendingFlushes = pendingFlushes.drop(1)
      }

    case Terminate =>
      terminate()

    case Terminated(_) | PeerClosed | Closed =>
      context stop self

    case ErrorClosed(cause) =>
      log.error(cause)
      context stop self
  }

  def terminate(): Unit = {
    connection ! Close
  }

  @tailrec
  def decodeUntilInsufficientBytes(in: ByteBuf, commands: Seq[Any] = Seq.empty[Any]): Seq[Any] = {
    if (!in.isReadable) {
      commands
    } else {
      decodeOne(upstream) match {
        case None => commands
        case Some(command) => decodeUntilInsufficientBytes(in, commands :+ command)
      }
    }
  }

  def decodeOne(in: ByteBuf) = {
    upstream.markReaderIndex()

    decoderOpt
      .map(_.decode(upstream))
      .flatMap {
        case MessageOutput(message) =>
          Some(message)

        case RequireMoreBytes =>
          upstream.resetReaderIndex()
          None
      }
  }

  case object FlushAck extends Tcp.Event

}
