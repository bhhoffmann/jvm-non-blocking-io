package no.bhhoffmann.nio.examples.weather

import java.time.LocalDateTime

class HttpRequest {

    fun sendResponse(forecast: Forecast) {
        // Pretend to send response
    }
}

data class Forecast(
    val description: String,
    val weather: List<Weather>
)

data class Weather(
    val time: LocalDateTime,
    val temperature: Int,
)

data class Coordinates(
    val lon: String, // longitude / lengdegrad / øst-vest
    val lat: String // latitude / breddegrad / nord-sør
)