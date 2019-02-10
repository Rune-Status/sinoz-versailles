package io.versailles.application.component.middleware

import java.net.InetSocketAddress

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.{Channel, ChannelHandler, ChannelOption, EventLoopGroup}
import io.netty.channel.epoll.{Epoll, EpollEventLoopGroup, EpollServerSocketChannel}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import scalaz.zio.{IO, Ref}

object Server {
  def create =
    for { chanRef <- Ref[Option[Channel]](None) } yield new Server(chanRef)
}

/**
  * The 'middleware' server that is publicly exposed for external clients
  * to connect with.
  * @author Sino
  */
final class Server(private val channelRef: Ref[Option[Channel]]) {
  /** Creates a [[ServerBootstrap]] that uses the given [[EventLoopGroup]]s and
    * delegates connected [[Channel]]s to the given [[ChannelHandler]]. */
  def createBootstrap(parent: EventLoopGroup, child: EventLoopGroup, handler: ChannelHandler) =
    for {
      bootstrap <- IO.succeed(newBootstrap)

      _         <- IO.sync(bootstrap.group(parent, child))
      _         <- IO.sync(bootstrap.childHandler(handler))

      _         <- IO.sync(bootstrap.childOption(ChannelOption.TCP_NODELAY, java.lang.Boolean.TRUE))
    } yield bootstrap

  /** Attempts to ignite this [[Server]] using the given [[ServerBootstrap]]
    * that should contain all of the required configurations. The [[Server]]
    * is to listen at the specified [[InetSocketAddress]]. */
  def ignite(bootstrap: ServerBootstrap, address: InetSocketAddress) =
    for {
      channel <- IO.syncException(bootstrap.bind(address).sync().channel())
      _       <- channelRef.set(Some(channel))
    } yield ()

  /** Awaits the termination of a currently bound [[Channel]] in this
    * [[Server]]. This operation does not actually terminate the channel but
    * simply waits until it does happen. */
  def awaitTermination =
    for {
      channelOpt  <- channelRef.get
      _           <- IO.sync(channelOpt.foreach(_.closeFuture().sync()))
    } yield ()

  /** Extinguishes this [[Server]] if it is running. Else, it will fail the
    * operation. */
  def extinguish() =
    for {
      channelOpt <- channelRef.get

      _          <- tryToExtinguish(channelOpt)
      _          <- channelRef.set(None)
    } yield ()

  /** Tries to extinguish the given set [[Channel]]. Fails the operation if the
    * [[Channel]] isn't set. The operation may fail regardless, should Netty
    * throw an [[Exception]] while closing down the [[Channel]]. */
  private def tryToExtinguish(channelOpt: Option[Channel]) =
    if (channelOpt.isEmpty) {
      IO.fail(new Exception("server channel isn't ignited"))
    } else {
      IO.syncThrowable(channelOpt.foreach(_.close().sync().get()))
    }

  /** Produces a new [[ServerBootstrap]] that is configured to create the
    * appropriate type of [[Channel]] upon binding. The selected [[Channel]]
    * type depends on whether E-poll is available or not. */
  private def newBootstrap =
    if (Epoll.isAvailable) {
      new ServerBootstrap().channel(classOf[EpollServerSocketChannel])
    } else {
      new ServerBootstrap().channel(classOf[NioServerSocketChannel])
    }

  /** Creates an event loop group with the amount of threads being equal
    * to the amount of processors that exist on the machine. */
  def createDefaultEventLoopGroup =
    createEventLoopGroup(Runtime.getRuntime.availableProcessors())

  /** Produces an event loop group with the specified amount of initial threads. */
  def createEventLoopGroup(amtThreads: Int) =
    if (Epoll.isAvailable) {
      IO.succeed(new EpollEventLoopGroup(amtThreads))
    } else {
      IO.succeed(new NioEventLoopGroup(amtThreads))
    }
}
