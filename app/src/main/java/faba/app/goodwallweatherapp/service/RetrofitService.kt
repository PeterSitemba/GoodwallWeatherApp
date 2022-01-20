package faba.app.goodwallweatherapp.service

import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastData
import faba.app.goodwallweatherapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.Single

interface RetrofitService {

    @GET(Constants.CURRENT_URL)
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): Single<WeatherData>

    @GET(Constants.FORECAST_URL)
    fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String,
    ): Single<ForecastData>


}