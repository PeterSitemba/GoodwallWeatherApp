package faba.app.goodwallweatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CurrentWeatherData::class, ForecastWeatherData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class WeatherRoomDatabase : RoomDatabase() {
    abstract fun WeatherDao(): WeatherDao
}