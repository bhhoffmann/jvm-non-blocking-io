package no.bhhoffmann.nio.examples.socket

import no.bhhoffmann.nio.examples.log
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel


@Throws(IOException::class)
fun main() {
    val port = 9090
    var active = true

    val serverSocketChannel: ServerSocketChannel = ServerSocketChannel.open()
    serverSocketChannel.bind(InetSocketAddress("localhost", port))
    while (active) {
        log("Accepting connections")
        val socketChannel: SocketChannel = serverSocketChannel.accept() // blocking
        log("Connection accepted")
        val buffer: ByteBuffer = ByteBuffer.allocate(1024)
        log("Echo loop")
        while (true) {
            buffer.clear()
            val read: Int = socketChannel.read(buffer) // blocking
            log("Read data - writing back")
            if (read < 0) {
                break
            }
            buffer.flip()
            socketChannel.write(buffer) // blocking
        }
        log("Closing connection")
        socketChannel.close()
    }
    serverSocketChannel.close()
}