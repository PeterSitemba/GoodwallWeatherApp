package faba.app.goodwallweatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastData
import faba.app.goodwallweatherapp.models.forecast.ForecastDays
import faba.app.goodwallweatherapp.repository.WeatherRepository
import faba.app.goodwallweatherapp.service.APIResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    var currentWeather: LiveData<APIResponse<WeatherData>>
        private set

    var forecastWeather: LiveData<APIResponse<ForecastData>>
        private set

    private val mutableSelectedForecast = MutableLiveData<ForecastDays>()
    val selectedForecast: LiveData<ForecastDays> get() = mutableSelectedForecast

    val loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val currentTempForecast: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val fullForecastList: MutableLiveData<MutableList<ForecastDays>>  by lazy{
        MutableLiveData<MutableList<ForecastDays>>()
    }




    fun selectForecast(forecastDays: ForecastDays) {
        mutableSelectedForecast.value = forecastDays
    }

    init {
        currentWeather = repository.currentWeatherDataResponse
        forecastWeather = repository.forecastWeatherDataResponse
        loading.value = true
        currentTempForecast.value = ""
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

    fun getRowCount(): Flow<Int?>? {
        return repository.getRowCount()
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }
}