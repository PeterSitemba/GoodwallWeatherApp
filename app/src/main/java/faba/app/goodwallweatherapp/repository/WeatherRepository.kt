package faba.app.goodwallweatherapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import faba.app.goodwallweatherapp.database.WeatherDao
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastData
import faba.app.goodwallweatherapp.service.APIResponse
import faba.app.goodwallweatherapp.service.NetworkConnectionInterceptor
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
    private val weatherDao: WeatherDao,
    private val context: Context
) {

    private val disposable = CompositeDisposable()
    val currentWeatherDataResponse = MutableLiveData<APIResponse<WeatherData>>()
    val forecastWeatherDataResponse = MutableLiveData<APIResponse<ForecastData>>()

    private fun insertCurrent(weatherData: WeatherData) {
        weatherDao.insertCurrentWeather(weatherData)
    }

    private fun insertForecast(forecastData: ForecastData) {
        weatherDao.insertForecastWeather(forecastData)
    }

    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        appId: String
    ) {
        val currentWeatherDataObservable: Single<WeatherData> =
            retrofitService.getCurrentWeather(lat, lon, appId, units = "metric")

        val apiObservable = currentWeatherDataObservable
            .toObservable().doOnNext {
                var weatherData = WeatherData(
                    1,
                    it.base,
                    it.clouds,
                    it.cod,
                    it.coord,
                    it.dt,
                    it.main,
                    it.name,
                    it.sys,
                    it.timezone,
                    it.visibility,
                    it.weather,
                    it.wind
                )
                insertCurrent(weatherData)
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
        if (NetworkConnectionInterceptor(context).isNetworkAvailable()) {
            Log.e("Weather repo", "Internet available")

            disposable.add(
                Observable.concat(db, remote)
                    .filter { false }
                    .timeout(100, TimeUnit.MILLISECONDS)
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

        } else {
            Log.e("Weather repo", "No Internet")
            disposable.add(
                db.subscribeOn(Schedulers.newThread())
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

    }

    private fun observeForecast(
        db: Observable<ForecastData>,
        remote: Observable<ForecastData>
    ) {

        if (NetworkConnectionInterceptor(context).isNetworkAvailable()) {
            disposable.add(
                Observable.concat(db, remote)
                    .filter { false }
                    .timeout(100, TimeUnit.MILLISECONDS)
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
        } else {
            disposable.add(
                db.subscribeOn(Schedulers.newThread())
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
    }

    fun clear() {
        disposable.clear()
    }


}