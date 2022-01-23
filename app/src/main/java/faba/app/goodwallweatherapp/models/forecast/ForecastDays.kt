package faba.app.goodwallweatherapp.models.forecast

import android.os.Parcelable
import faba.app.goodwallweatherapp.models.current.Weather
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ForecastDays(
    val clouds: @RawValue Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: @RawValue MainForecast,
    val pop: Double,
    val sys: @RawValue SysForecast,
    val visibility: Int,
    val weather: @RawValue List<Weather>,
    val wind: @RawValue WindForecast
) : Parcelable