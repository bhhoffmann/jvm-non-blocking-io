package no.bhhoffmann.nio.examples.weather

import no.bhhoffmann.nio.examples.log
import java.util.concurrent.CompletableFuture
import kotlin.random.Random

val coordOslo = Coordinates("10.74609", "59.91273")
val coordBarcelona = Coordinates("2.154007", "41.390205")

class LocationService {

    private val locationToCoordinates = mapOf(
        "Oslo" to coordOslo,
        "Barcelona" to coordBarcelona,
    )

    // Pretend DB access (blocking I/O)
    private fun coordinates(location: String): Coordinates {
        val delay = Random.nextLong(100, 400)
        log("Retrieving coordinates")
        Thread.sleep(delay)
        log("Retrieved coordinates in $delay ms")
        return locationToCoordinates[location]
            ?: error("No coordinates for location=$location")
    }

    fun getCoordinates(location: String): Coordinates {
        return coordinates(location)
    }

    fun getCoordinatesCallback(
        location: String,
        callback: (Coordinates) -> Unit
    ) {
        ioExecutor.submit {
            val coordinates = coordinates(location)
            callback(coordinates)
        }
    }

    fun getCoordinatesFuture(
        location: String
    ): CompletableFuture<Coordinates> {
        val future = CompletableFuture<Coordinates>()
        ioExecutor.submit {
            val coordinates = coordinates(location)
            future.complete(coordinates)
        }
        return future
    }

}