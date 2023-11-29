package no.bhhoffmann.nio.examples.weather

import no.bhhoffmann.nio.examples.log
import java.util.concurrent.Executors

fun main() {
    System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1")
    val executor = Executors.newVirtualThreadPerTaskExecutor()

    log("Starting")

    executor.submit {
        forecast("Oslo")
    }

    executor.submit {
        forecast("Barcelona")
    }

    executor.submit {
        log("DOING SOMETHING ELSE")
    }

    Thread.sleep(5000)
}