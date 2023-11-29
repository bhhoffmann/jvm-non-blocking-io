package no.bhhoffmann.nio.examples.weather

import no.bhhoffmann.nio.examples.log
import java.util.concurrent.CompletableFuture


private val locationService = LocationService()
private val weatherService = WeatherService()

fun weatherForecastFuture(location: String): Forecast {
    val weatherFuture = locationService.getCoordinatesFuture(location)
        .thenCompose { coordinates ->
            weatherService.getWeatherFuture(coordinates)
        }

    val weather = weatherFuture.join()

    val currentWeather = weather.first()
    val description = if (currentWeather.temperature < 0) {
        "Freezing!"
    } else "Not too bad"

    return Forecast(
        description,
        weather
    )
}

fun weatherForecastMultipleLocations(
    locationA: String,
    locationB: String,
): List<Forecast> {
    val forecastAFuture = locationService.getCoordinatesFuture(locationA)
        .thenCompose { coordinates ->
            weatherService.getWeatherFuture(coordinates)
        }
        .thenCompose { weather ->
            CompletableFuture.completedFuture(
                weatherToForecast(weather)
            )
        }

    val forecastBFuture = locationService.getCoordinatesFuture(locationB)
        .thenCompose { coordinates ->
            weatherService.getWeatherFuture(coordinates)
        }
        .thenCompose { weather ->
            CompletableFuture.completedFuture(
                weatherToForecast(weather)
            )
        }


    val forecastA = forecastAFuture.join()
    val forecastB = forecastBFuture.join()

    log("Retrieved all forecasts")

    return listOf(
        forecastA,
        forecastB
    )
}

private fun weatherToForecast(weather: List<Weather>): Forecast {
    val currentWeather = weather.first()
    val description = if (currentWeather.temperature < 0) {
        "Freezing!"
    } else "Not too bad"

    return Forecast(description, weather)
}


fun main() {
    weatherForecastMultipleLocations("Oslo", "Barcelona")
}