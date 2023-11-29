package no.bhhoffmann.nio.examples.weather

import no.bhhoffmann.nio.examples.log
import java.util.concurrent.Executors


private val locationService = LocationService()
private val weatherService = WeatherService()


fun forecast(location: String): Forecast {
    val coordinates = locationService.getCoordinates(location)

    val weather = weatherService.getWeather(coordinates)

    val currentWeather = weather.first()
    val description = if (currentWeather.temperature < 0) {
        "Freezing!"
    } else "Not too bad"

    return Forecast(description, weather)
}


fun main() {
    val executor = Executors.newSingleThreadExecutor()
    executor.submit {
        forecast("Oslo")
    }

    executor.submit {
        forecast("Barcelona")
    }

    executor.submit {
        log("DOING SOMETHING ELSE")
    }
}