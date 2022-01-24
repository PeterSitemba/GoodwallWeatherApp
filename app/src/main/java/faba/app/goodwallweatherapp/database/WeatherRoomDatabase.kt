package faba.app.goodwallweatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastData

@Database(entities = [WeatherData::class, ForecastData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class WeatherRoomDatabase : RoomDatabase() {
    abstract fun WeatherDao(): WeatherDao
}