package io.versailles.application.component.middleware

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import io.versailles.application.component.middleware.ChannelSupervisor.ConnectionEstablished
import io.versailles.application.component.middleware.Server.{BindTo, Terminate}

object Server {
  case class BindTo(addr: InetSocketAddress)
  case object Terminate

  def props(channelSupervisor: ActorRef) = Props(new Server(channelSupervisor))
}

/**
  * Provides a TCP server for the middleware of this application.
  * @author Sino
  */
final class Server(channelSupervisor: ActorRef) extends Actor with ActorLogging {
  override def receive =
    unbound

  def unbound: Receive = {
    case BindTo(addr) =>
      IO(Tcp)(context.system) ! Bind(self, addr)

    case CommandFailed(x: Bind) =>
      log.error(s"Failed to bind to address ${x.localAddress}")

    case Bound(local) =>
      log.info(s"Server bound ready for incoming connections at $local")
      context.become(bound(sender()))
  }

  def bound(connectionManager: ActorRef): Receive = {
    case Connected(remote, _) =>
      channelSupervisor ! ConnectionEstablished(sender(), remote)

    case Terminate =>
      connectionManager ! Unbind

    case Unbound =>
      log.info("Server unbound and stopped listening for connections")

      channelSupervisor ! PoisonPill
      context.become(unbound)
  }
}
