package faba.app.goodwallweatherapp.models.current

data class SysCurrent(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)