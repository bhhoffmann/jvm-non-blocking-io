package no.bhhoffmann.nio.examples.socket

import no.bhhoffmann.nio.examples.log
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel


fun main() {
    val port = 9090
    var active = true

    val ports = 8
    val serverSocketChannels = arrayOfNulls<ServerSocketChannel>(ports)
    val selector: Selector = Selector.open()

    for (p in 0 until ports) {
        val serverSocketChannel = ServerSocketChannel.open()
        serverSocketChannels[p] = serverSocketChannel
        serverSocketChannel.configureBlocking(false)
        serverSocketChannel.bind(InetSocketAddress("localhost", port + p))
        log("Registering serverSocketChannel with selector for accepting connections")
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT)
    }

    while (active) {
        log("Waiting on seletor")
        selector.select() // blocking
        val keysIterator: MutableIterator<SelectionKey> = selector.selectedKeys().iterator()
        log("Selector got events")
        var nrEvents = 0
        while (keysIterator.hasNext()) {
            ++nrEvents
            val key = keysIterator.next()
            if (key.isAcceptable) {
                log("Handling isAcceptable")
                accept(selector, key)
            }
            if (key.isReadable) {
                log("Handling isReadable")
                keysIterator.remove()
                read(selector, key)
            }
            if (key.isWritable) {
                log("Handling isWritable")
                keysIterator.remove()
                write(key)
            }
        }
        log("Handled $nrEvents selector events")
    }
    for (serverSocketChannel in serverSocketChannels) {
        serverSocketChannel!!.close()
    }
}

private fun accept(selector: Selector, key: SelectionKey) {
    val serverSocketChannel = key.channel() as ServerSocketChannel
    log("Accepting connection")
    val socketChannel: SocketChannel? = serverSocketChannel.accept() // can be non-blocking
    if (socketChannel != null) {
        socketChannel.configureBlocking(false)
        socketChannel.register(selector, SelectionKey.OP_READ)
    }
}

private fun read(selector: Selector, key: SelectionKey) {
    val socketChannel = key.channel() as SocketChannel
    val buffer: ByteBuffer = ByteBuffer.allocate(1024)
    log("Reading data")
    socketChannel.read(buffer) // can be non-blocking
    buffer.flip()
    log("Register write back operation with selector")
    socketChannel.register(selector, SelectionKey.OP_WRITE, buffer)
}

private fun write(key: SelectionKey) {
    val socketChannel = key.channel() as SocketChannel
    val buffer = key.attachment() as ByteBuffer
    log("Writing data")
    socketChannel.write(buffer) // can be non-blocking
    socketChannel.close()
}