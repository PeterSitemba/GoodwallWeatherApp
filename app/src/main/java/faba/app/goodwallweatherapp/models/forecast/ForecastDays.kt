package faba.app.goodwallweatherapp.models.forecast

import faba.app.goodwallweatherapp.models.current.Weather

data class ForecastDays(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: MainForecast,
    val pop: Double,
    val sys: SysForecast,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: WindForecast
)