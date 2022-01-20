package faba.app.goodwallweatherapp.models.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import faba.app.goodwallweatherapp.database.Converter

@Entity(tableName = "weather_forecast_table")
data class ForecastData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @TypeConverters(Converter::class) val city: City,
    val cnt: Int,
    val cod: String,
    @TypeConverters(Converter::class) val list: List<ForecastDays>,
    val message: Int
)