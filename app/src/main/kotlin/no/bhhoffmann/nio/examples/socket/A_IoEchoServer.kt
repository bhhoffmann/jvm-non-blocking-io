package no.bhhoffmann.nio.examples.socket

import no.bhhoffmann.nio.examples.log
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket


fun main() {
    val port = 9090
    var active = true

    val serverSocket = ServerSocket(port)
    while (active) {
        log("Accepting connections")
        val socket: Socket = serverSocket.accept() // blocking
        log("Connection accepted")
        val inStream: InputStream = socket.getInputStream()
        val outStream: OutputStream = socket.getOutputStream()
        var read: Int
        val bytes = ByteArray(1024)
        log("Echo loop")
        while (inStream.read(bytes).also { read = it } != -1) { // blocking
            log("Read data - writing back")
            outStream.write(bytes, 0, read) // blocking
        }
        log("Closing connection")
        socket.close()
    }
    serverSocket.close()
}