package io.versailles.application.component.middleware

import java.net.InetSocketAddress

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props}
import akka.io.Tcp.Register
import io.netty.buffer.ByteBufAllocator
import io.versailles.application.component.middleware.Channel.{SetDecoder, SetEncoder, SetHandler}
import io.versailles.application.component.middleware.ChannelSupervisor.{ConnectionEstablished, DecoderProducer, EncoderProducer, HandlerProducer}

object ChannelSupervisor {
  case class ConnectionEstablished(connection: ActorRef, address: InetSocketAddress)

  private type Channel = ActorRef

  type HandlerProducer = Channel => Props
  type DecoderProducer = () => MessageDecoder
  type EncoderProducer = () => MessageEncoder[_ <: Any]

  def props(handlerProducer: HandlerProducer, decoderProducer: DecoderProducer, encoderProducer: EncoderProducer) =
    Props(new ChannelSupervisor(handlerProducer, decoderProducer, encoderProducer))
}

/**
  * Supervises over [[Channel]] actors by deciding what to do when
  * a [[Channel]] is failing. It also knows how to properly configure
  * a [[Channel]] when a new connection is established.
  * @author Sino
  */
final class ChannelSupervisor(produceHandler: HandlerProducer, produceDecoder: DecoderProducer, produceEncoder: EncoderProducer) extends Actor {
  private val bufferAllocator = ByteBufAllocator.DEFAULT

  override def supervisorStrategy = OneForOneStrategy() {
    case _: Exception => Stop
  }

  override def receive = {
    case ConnectionEstablished(connection, address) =>
      val channel = context.actorOf(Channel.props(connection, address, bufferAllocator))

      channel ! SetHandler(produceHandler(channel))

      channel ! SetDecoder(produceDecoder())
      channel ! SetEncoder(produceEncoder())

      connection ! Register(channel)
  }
}
