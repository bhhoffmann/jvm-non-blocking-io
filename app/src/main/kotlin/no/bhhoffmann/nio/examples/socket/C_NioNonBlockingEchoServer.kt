package no.bhhoffmann.nio.examples.socket

import no.bhhoffmann.nio.examples.log
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel


fun main() {
    val port = 9090
    var active = true

    val serverSocketChannel: ServerSocketChannel = ServerSocketChannel.open()
    serverSocketChannel.configureBlocking(false)
    serverSocketChannel.bind(InetSocketAddress(port))
    while (active) {
        log("Accepting connections")
        val socketChannel: SocketChannel? = serverSocketChannel.accept() // non-blocking
        if (socketChannel != null) {
            log("Connection accepted")
            socketChannel.configureBlocking(false)
            val buffer: ByteBuffer = ByteBuffer.allocate(1024)
            log("Echo loop")
            while (true) {
                log("Reading data if available")
                buffer.clear()
                val read: Int = socketChannel.read(buffer) // non-blocking
                if (read < 0) {
                    break
                } else if (read > 0) {
                    log("Read data - writing back")
                }
                buffer.flip()
                socketChannel.write(buffer) // can be non-blocking
                Thread.sleep(1000)
            }
            log("Closing connection")
            socketChannel.close()
        } else Thread.sleep(1000)
    }
    serverSocketChannel.close()
}