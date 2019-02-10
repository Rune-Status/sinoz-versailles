package io.versailles.application.component.middleware.handler

import com.twitter.conversions.StorageUnitOps._
import io.netty.channel.{ChannelFutureListener, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.handler.codec.FixedLengthFrameDecoder
import io.versailles.application.command.{ConnectToAssetService, ConnectToLoginService}
import io.versailles.application.component.login.LoginService
import io.versailles.application.component.middleware.encoding.{AssetFolderEjectionEncoder, AssetFolderRequestDecoder, LoginRequestDecoder, LoginResponseEncoder}
import io.versailles.application.event.ServiceResponse
import io.versailles.application.event.ServiceResponse.{ClientOutOfDate, MayProceed}
import io.versailles.application.model.{ClientVersion, CredentialBlockKeySet, Nonce}
import io.versailles.application.storage.Storage

/**
  * The application logic handler that decides what to do with messages
  * related to service connect attempts.
  * @author Sino
  */
// TODO find a different way to solve this. too many dependencies
final class ServiceConnectHandler(loginService: LoginService, assetStorage: Storage, expectedVersion: ClientVersion, archiveCount: Int, credentialsKeySet: CredentialBlockKeySet) extends ChannelInboundHandlerAdapter {
  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit =
    msg match {
      case ConnectToAssetService(version) => onAssetServiceConnect(ctx, version)
      case ConnectToLoginService          => onLoginServiceConnect(ctx)
    }

  /** Accepts or rejects attempts made to connect to the asset service, depending on
    * if the [[ClientVersion]] is up to with the [[expectedVersion]]. */
  private def onAssetServiceConnect(ctx: ChannelHandlerContext, version: ClientVersion): Unit =
    if (version.isUpToDateWith(expectedVersion)) {
      acceptAssetServiceConnect(ctx)
    } else {
      rejectAssetServiceConnect(ctx, reason = ClientOutOfDate)
    }

  /** Accepts the request by writing a [[MayProceed]] response back to the
    * client. Once done, the pipeline is updated accordingly as preparation
    * for the next stage in line. */
  private def acceptAssetServiceConnect(ctx: ChannelHandlerContext): Unit = {
    ctx.writeAndFlush(MayProceed(nonce = None))

    // the data that is to be decoded in AssetFolderRequestDecoder are all
    // to be read in blocks of 4 bytes, so let's have FixedLengthFrameDecoder
    // make sure data comes in blocks of 4 bytes so AssetFolderRequestDecoder
    // doesn't have to
    ctx.pipeline().addBefore("decoder", "frame-decoder", new FixedLengthFrameDecoder(4))
    ctx.pipeline().replace("decoder", "decoder", new AssetFolderRequestDecoder)

    ctx.pipeline().replace("encoder", "encoder", new AssetFolderEjectionEncoder)
    ctx.pipeline().replace("handler", "handler", new AssetStreamingHandler(assetStorage, 1024.kilobytes))
  }

  /** Rejects the request by writing the given [[ServiceResponse.Type]] back
    * to the client. The channel is to be closed after. */
  private def rejectAssetServiceConnect(ctx: ChannelHandlerContext, reason: ServiceResponse.Type): Unit = {
    ctx.writeAndFlush(reason).addListener(ChannelFutureListener.CLOSE)
  }

  /** Accepts or rejects attempts made to connect to the login service. */
  private def onLoginServiceConnect(ctx: ChannelHandlerContext): Unit = {
    acceptLoginServiceConnect(ctx)
  }

  /** Accepts the request by writing a [[MayProceed]] response back to the
    * client. Once done, the pipeline is updated accordingly as preparation
    * for the next stage in line. */
  private def acceptLoginServiceConnect(ctx: ChannelHandlerContext): Unit = {
    val nonce = Nonce.generate

    ctx.writeAndFlush(MayProceed(Some(nonce)))

    ctx.pipeline().replace("decoder", "decoder", new LoginRequestDecoder(archiveCount, credentialsKeySet))
    ctx.pipeline().replace("encoder", "encoder", new LoginResponseEncoder)

    ctx.pipeline().replace("handler", "handler", new LoginRequestHandler(loginService))
  }
}
