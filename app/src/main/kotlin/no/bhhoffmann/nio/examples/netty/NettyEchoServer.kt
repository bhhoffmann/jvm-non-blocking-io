package no.bhhoffmann.nio.examples.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import no.bhhoffmann.nio.examples.log

fun main(args: Array<String>) {
    val port = args.firstOrNull()?.toInt() ?: 9090
    log("Starting Netty server on port=$port")
    nettyServer(port, EchoHandler())
}

fun nettyServer(port: Int, handler: ChannelHandler) {
    val bossGroup: EventLoopGroup = NioEventLoopGroup()
    val workerGroup: EventLoopGroup = NioEventLoopGroup()
    try {
        val b = ServerBootstrap()
        b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                public override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast(handler)
                }
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        // Bind and start to accept incoming connections.
        val f = b.bind(port).sync()

        // Wait until the server socket is closed.
        // In this example, this does not happen, but you can do that to gracefully
        // shut down your server.
        f.channel().closeFuture().sync()
    } finally {
        workerGroup.shutdownGracefully()
        bossGroup.shutdownGracefully()
    }
}

class EchoHandler : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        ctx.write(msg)
        ctx.flush()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // Close the connection when an exception is raised.
        cause.printStackTrace()
        ctx.close()
    }

}