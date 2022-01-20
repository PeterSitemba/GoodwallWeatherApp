package faba.app.goodwallweatherapp.models.forecast

import faba.app.goodwallweatherapp.models.current.Coord

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)