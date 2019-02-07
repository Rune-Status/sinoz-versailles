package io.unity.application.component.middleware.handler

import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}
import io.unity.application.command.RequestLogin
import io.unity.application.service.LoginService

/**
  * The application logic handler that serves as the mediator between the
  * given [[LoginService]] and the middleware layer.
  * @author Sino
  */
final class LoginRequestHandler(loginService: LoginService) extends SimpleChannelInboundHandler[RequestLogin] {
  override def channelRead0(ctx: ChannelHandlerContext, msg: RequestLogin) = {
    // TODO
  }

  // TODO validate login request
}
