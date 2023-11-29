package no.bhhoffmann.nio.examples.weather

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val ioExecutor: ExecutorService = Executors.newFixedThreadPool(5)