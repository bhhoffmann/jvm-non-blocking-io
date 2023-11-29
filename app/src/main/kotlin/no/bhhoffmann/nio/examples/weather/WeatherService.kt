package no.bhhoffmann.nio.examples.weather

import no.bhhoffmann.nio.examples.log
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import kotlin.random.Random

class WeatherService {

    private val coordinatesToWeather = mapOf(
        coordOslo to listOf(
            Weather(
                LocalDateTime.now(),
                -5
            ),
            Weather(
                LocalDateTime.now().plusDays(1),
                -3
            )
        ),

        coordBarcelona to listOf(
            Weather(
                LocalDateTime.now(),
                19
            ),
            Weather(
                LocalDateTime.now().plusDays(1),
                16
            )
        )
    )

    private fun weather(coordinates: Coordinates): List<Weather> {
        val delay = Random.nextLong(500, 1000)
        log("Retrieving weather")
        Thread.sleep(delay)
        log("Retrieved weather in $delay ms")
        return coordinatesToWeather[coordinates] ?: error("No weather for coordinates")
    }

    fun getWeather(coordinates: Coordinates): List<Weather> {
        return weather(coordinates)
    }

    fun getWeatherCallback(
        coordinates: Coordinates,
        callback: (List<Weather>) -> Unit
    ) {
        ioExecutor.submit {
            val weather = weather(coordinates)
            callback(weather)
        }
    }

    fun getWeatherFuture(
        coordinates: Coordinates
    ): CompletableFuture<List<Weather>> {
        val future = CompletableFuture<List<Weather>>()
        ioExecutor.submit {
            val weather = weather(coordinates)
            future.complete(weather)
        }
        return future
    }
}