package io.versailles.application.component.middleware.handler

import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}
import io.versailles.application.command.RequestLogin
import io.versailles.application.component.login.LoginService

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
