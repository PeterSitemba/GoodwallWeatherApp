package faba.app.goodwallweatherapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import faba.app.goodwallweatherapp.models.current.*
import faba.app.goodwallweatherapp.models.forecast.City
import faba.app.goodwallweatherapp.models.forecast.ForecastDays


class Converter {

    @TypeConverter
    fun toClouds(json: String?): Clouds? {
        val type = object : TypeToken<Clouds>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJsonClouds(clouds: Clouds?): String {
        val type = object : TypeToken<Clouds>() {}.type
        return Gson().toJson(clouds, type)
    }

    @TypeConverter
    fun toCoord(json: String?): Coord? {
        val type = object : TypeToken<Coord>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJsonCoord(coord: Coord?): String {
        val type = object : TypeToken<Coord>() {}.type
        return Gson().toJson(coord, type)
    }

    @TypeConverter
    fun toMainCurrent(json: String?): MainCurrent? {
        val type = object : TypeToken<MainCurrent>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJsonMainCurrent(main: MainCurrent?): String {
        val type = object : TypeToken<MainCurrent>() {}.type
        return Gson().toJson(main, type)
    }

    @TypeConverter
    fun toSysCurrent(json: String?): SysCurrent? {
        val type = object : TypeToken<SysCurrent>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJsonSysCurrent(sys: SysCurrent?): String {
        val type = object : TypeToken<SysCurrent>() {}.type
        return Gson().toJson(sys, type)
    }

    @TypeConverter
    fun toWindCurrent(json: String?): WindCurrent? {
        val type = object : TypeToken<WindCurrent>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJsonWindCurrent(wind: WindCurrent?): String {
        val type = object : TypeToken<WindCurrent>() {}.type
        return Gson().toJson(wind, type)
    }

    @TypeConverter
    fun toWeather(json: String?): List<Weather>? {
        val type = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJsonWeather(weather: List<Weather>?): String {
        val type = object : TypeToken<List<Weather>>() {}.type
        return Gson().toJson(weather, type)
    }

    @TypeConverter
    fun toCity(json: String?): City? {
        val type = object : TypeToken<City>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJsonCity(city: City?): String {
        val type = object : TypeToken<City>() {}.type
        return Gson().toJson(city, type)
    }

    @TypeConverter
    fun toForecastDays(json: String?): List<ForecastDays>? {
        val type = object : TypeToken<List<ForecastDays>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toForecastDays(forecastDays: List<ForecastDays>?): String {
        val type = object : TypeToken<List<ForecastDays>>() {}.type
        return Gson().toJson(forecastDays, type)
    }


}