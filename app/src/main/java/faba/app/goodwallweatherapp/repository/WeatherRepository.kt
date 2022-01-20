package faba.app.goodwallweatherapp.repository

import androidx.lifecycle.MutableLiveData
import faba.app.goodwallweatherapp.database.WeatherDao
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastData
import faba.app.goodwallweatherapp.service.APIResponse
import faba.app.goodwallweatherapp.service.RetrofitService
import faba.app.goodwallweatherapp.utils.Status
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val retrofitService: RetrofitService,
    private val weatherDao: WeatherDao
) {

    private val disposable = CompositeDisposable()
    val currentWeatherDataResponse = MutableLiveData<APIResponse<WeatherData>>()
    val forecastWeatherDataResponse = MutableLiveData<APIResponse<ForecastData>>()


    /* fun getCurrentWeather(
         lat: Double,
         lon: Double,
         appId: String
     ) = retrofitService.getCurrentWeather(
         lat,
         lon,
         appId,
         units = "metric"
     )

     fun getWeatherForecast(
         lat: Double,
         lon: Double,
         appId: String
     ) = retrofitService.getWeatherForecast(
         lat,
         lon,
         appId,
         units = "metric"
     )*/

    fun insertCurrent(weatherData: WeatherData) {
        weatherDao.insertCurrentWeather(weatherData)
    }

    fun insertForecast(forecastData: ForecastData) {
        weatherDao.insertForecastWeather(forecastData)
    }

    val roomCurrentWeatherList = weatherDao.getAllCurrentWeatherData()
    val roomWeatherForecastList = weatherDao.getAllWeatherForecast()

    val getRowCount = weatherDao.getRowCount()
    val getForecastRowCount = weatherDao.getForecastRowCount()


    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        appId: String
    ) {
        val currentWeatherDataObservable: Single<WeatherData> =
            retrofitService.getCurrentWeather(lat, lon, appId, units = "metric")

        val apiObservable = currentWeatherDataObservable
            .toObservable().doOnNext {
                insertCurrent(it)
            }

        val dbObservable = weatherDao.getAllCurrentWeatherData()

        observeCurrent(dbObservable, apiObservable)

    }

    fun getWeatherForecast(
        lat: Double,
        lon: Double,
        appId: String
    ) {
        val currentWeatherDataObservable: Single<ForecastData> =
            retrofitService.getWeatherForecast(lat, lon, appId, units = "metric")

        val apiObservable = currentWeatherDataObservable
            .toObservable().doOnNext {
                insertForecast(it)
            }

        val dbObservable = weatherDao.getAllWeatherForecast()

        observeForecast(dbObservable, apiObservable)

    }


    private fun updateResponseCurrent(
        status: Status,
        data: WeatherData? = null,
        errorMessage: String? = null
    ) {
        val responseLoading = APIResponse(status, data, errorMessage)
        currentWeatherDataResponse.value = responseLoading
    }

    private fun updateResponseForecast(
        status: Status,
        data: ForecastData? = null,
        errorMessage: String? = null
    ) {
        val responseLoading = APIResponse(status, data, errorMessage)
        forecastWeatherDataResponse.value = responseLoading
    }


    private fun observeCurrent(
        db: Observable<WeatherData>,
        remote: Observable<WeatherData>
    ) {
        disposable.add(
            Observable.concat(db, remote)
                .filter { it.weather.isNotEmpty() }
                .timeout(400, TimeUnit.MILLISECONDS)
                .onErrorResumeNext(remote)
                .firstElement()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    updateResponseCurrent(Status.LOADING)
                }
                .subscribe(
                    {
                        updateResponseCurrent(Status.SUCCESS, it)
                    },
                    {
                        updateResponseCurrent(Status.ERROR, null, it.localizedMessage)
                    }

                ))
    }

    private fun observeForecast(
        db: Observable<ForecastData>,
        remote: Observable<ForecastData>
    ) {
        disposable.add(
            Observable.concat(db, remote)
                .timeout(400, TimeUnit.MILLISECONDS)
                .onErrorResumeNext(remote)
                .firstElement()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    updateResponseForecast(Status.LOADING)
                }
                .subscribe(
                    {
                        updateResponseForecast(Status.SUCCESS, it)
                    },
                    {
                        updateResponseForecast(Status.ERROR, null, it.localizedMessage)
                    }

                ))
    }

    fun clear() {
        disposable.clear()
    }


}