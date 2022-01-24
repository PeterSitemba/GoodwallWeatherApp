package faba.app.goodwallweatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastData
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface WeatherDao {

    //current
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(weatherData: WeatherData)

    @Query("SELECT * FROM current_weather_table")
    fun getAllCurrentWeatherData(): Observable<WeatherData>

    @Query("DELETE FROM current_weather_table")
    fun deleteAll()

    @Query("SELECT COUNT(id) FROM current_weather_table")
    fun getRowCount(): Flow<Int?>?

    fun getRowCountDistinct() = getRowCount()?.distinctUntilChanged()




    //forecast
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecastWeather(forecastData: ForecastData)

    @Query("SELECT * FROM weather_forecast_table")
    fun getAllWeatherForecast(): Observable<ForecastData>

    @Query("DELETE FROM weather_forecast_table")
    fun deleteAllForecast()

    @Query("SELECT COUNT(id) FROM weather_forecast_table")
    fun getForecastRowCount(): Observable<Int?>?

}