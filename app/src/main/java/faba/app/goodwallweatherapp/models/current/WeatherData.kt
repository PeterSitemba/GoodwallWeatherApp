package faba.app.goodwallweatherapp.models.current

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import faba.app.goodwallweatherapp.database.Converter

@Entity(tableName = "current_weather_table")
data class WeatherData(
    @PrimaryKey val id: Int = 1,
    val base: String,
    @TypeConverters(Converter::class) val clouds: Clouds,
    val cod: Int,
    @TypeConverters(Converter::class) val coord: Coord,
    val dt: Int,
    @TypeConverters(Converter::class) val main: MainCurrent,
    val name: String,
    @TypeConverters(Converter::class) val sys: SysCurrent,
    val timezone: Int,
    val visibility: Int,
    @TypeConverters(Converter::class) val weather: List<Weather>,
    @TypeConverters(Converter::class) val wind: WindCurrent
)