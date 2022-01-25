package faba.app.goodwallweatherapp.repository

import faba.app.goodwallweatherapp.models.current.*

class FakeRepo {
     val currentWeatherData = WeatherData(
        base = "stations",
        clouds = Clouds(0),
        cod = 200,
        coord = Coord(-122.0841, 37.4221),
        dt = 1643065669,
        id = 5375480,
        main = MainCurrent(289.77, 51, 1018, 290.63, 293.71, 286.24),
        name = "Mountain View",
        sys = SysCurrent("US", 2003086, 1643037439, 1643073799, 2),
        timezone = -28800,
        visibility = 10000,
        weather = listOf(Weather("clear sky", "01d", 800, "Clear")),
        wind = WindCurrent(14, 0.89)

    )

}