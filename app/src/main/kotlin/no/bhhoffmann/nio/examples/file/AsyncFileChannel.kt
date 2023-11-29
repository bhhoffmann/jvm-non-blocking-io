package no.bhhoffmann.nio.examples.file

import no.bhhoffmann.nio.examples.log
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.channels.CompletionHandler
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.toPath

fun main() {
    val resourceUrl: URL = object {}.javaClass.classLoader.getResource("hello.txt")
        ?: error("No classpath resource found at the provided path")

    log("Opening AsynchronousFileChannel")
    val afChannel = AsynchronousFileChannel.open(resourceUrl.toURI().toPath(), StandardOpenOption.READ)

    val buffer: ByteBuffer = ByteBuffer.allocate(1048)
    val readCompletionHandler = ReadCompletionHandler(buffer)
    log("Starting read and passing callback")
    afChannel.read(buffer, 0, Unit, readCompletionHandler)

    readln() // So that application does not exit
}


class ReadCompletionHandler(
    private val buffer: ByteBuffer
) : CompletionHandler<Int, Unit?> {
    override fun completed(result: Int, attachment: Unit?) {
        log("Nr of bytes read: $result")
        buffer.flip()
        val content = String(buffer.array()).trim { it <= ' ' }
        log("Content: $content")
    }

    override fun failed(exc: Throwable?, attachment: Unit?) {
        TODO("Not yet implemented")
    }

}