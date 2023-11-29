package no.bhhoffmann.nio.examples.weather


private val locationService = LocationService()
private val weatherService = WeatherService()

fun weatherForecastCallback(location: String, request: HttpRequest) {
    locationService.getCoordinatesCallback(location) { coordinates ->
        weatherService.getWeatherCallback(coordinates) { weather ->
            val currentWeather = weather.first()
            val description = if (currentWeather.temperature < 0) {
                "Freezing!"
            } else "Not too bad"

            request.sendResponse(
                Forecast(
                    description,
                    weather
                )
            )
        }
    }
}


fun main() {
    weatherForecastCallback("Oslo", HttpRequest())
    weatherForecastCallback("Barcelona", HttpRequest())
}