package faba.app.goodwallweatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastData
import faba.app.goodwallweatherapp.repository.WeatherRepository
import faba.app.goodwallweatherapp.service.APIResponse
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    var currentWeather: LiveData<APIResponse<WeatherData>>
        private set

    var forecastWeather: LiveData<APIResponse<ForecastData>>
        private set

    init {
        currentWeather = repository.currentWeatherDataResponse
        forecastWeather = repository.forecastWeatherDataResponse
    }

    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        appId: String
    ) {
        repository.getCurrentWeather(lat, lon, appId)
    }

    fun getWeatherForecast(
        lat: Double,
        lon: Double,
        appId: String
    ) {
        repository.getWeatherForecast(lat, lon, appId)
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }
}