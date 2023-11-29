package no.bhhoffmann.nio.examples

fun log(message: String) {
    println("${Thread.currentThread()} - $message")
}