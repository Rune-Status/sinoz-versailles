package io.unity.application.component.middleware.handler

import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}

/**
  * TODO
  * @author Sino
  */
final class GameCommandHandler extends ChannelInboundHandlerAdapter {
  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    // TODO
  }
}
