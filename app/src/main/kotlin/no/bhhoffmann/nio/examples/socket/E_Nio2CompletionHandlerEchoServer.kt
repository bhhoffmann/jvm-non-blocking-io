package no.bhhoffmann.nio.examples.socket

import no.bhhoffmann.nio.examples.log
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler


fun main() {
    val port = 9090
    val serverSocketChannel = AsynchronousServerSocketChannel.open()
    serverSocketChannel.bind(InetSocketAddress(port))
    val acceptCompletionHandler = AcceptCompletionHandler(serverSocketChannel)
    log("Starting accept and passing callback")
    serverSocketChannel.accept<Unit>(null, acceptCompletionHandler)
    readln() // Stop the application from exiting
}


class AcceptCompletionHandler(
    private val serverSocketChannel: AsynchronousServerSocketChannel
) : CompletionHandler<AsynchronousSocketChannel, Unit?> {
    override fun completed(socketChannel: AsynchronousSocketChannel, attachment: Unit?) {
        log("Accept complete")
        serverSocketChannel.accept<Unit>(null, this) // non-blocking
        val buffer: ByteBuffer = ByteBuffer.allocate(1024)
        val readCompletionHandler = ReadCompletionHandler(socketChannel, buffer)
        log("Starting read and passing callback")
        socketChannel.read<Unit>(buffer, null, readCompletionHandler) // non-blocking
    }

    override fun failed(t: Throwable, attachment: Unit?) {
        // exception handling
    }
}


internal class ReadCompletionHandler(
    private val socketChannel: AsynchronousSocketChannel,
    private val buffer: ByteBuffer
) : CompletionHandler<Int, Unit?> {
    override fun completed(bytesRead: Int, attachment: Unit?) {
        log("Read complete")
        val writeCompletionHandler = WriteCompletionHandler(socketChannel)
        buffer.flip()
        log("Starting write and passing callback")
        socketChannel.write<Unit>(buffer, null, writeCompletionHandler) // non-blocking
    }

    override fun failed(t: Throwable, attachment: Unit?) {
        // exception handling
    }
}


internal class WriteCompletionHandler(
    private val socketChannel: AsynchronousSocketChannel
) : CompletionHandler<Int, Unit?> {
    override fun completed(bytesWritten: Int, attachment: Unit?) {
        log("Write complete")
        try {
            socketChannel.close()
        } catch (e: IOException) {
            // exception handling
        }
    }

    override fun failed(t: Throwable, attachment: Unit?) {
        // exception handling
    }
}