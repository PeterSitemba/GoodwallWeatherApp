package faba.app.goodwallweatherapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    @SuppressLint("SimpleDateFormat")
    fun getDay(theDate: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = formatter.parse(theDate)
        return SimpleDateFormat("EEEE").format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDayOfWeek(): String {
        val dayOfWeek = Calendar.getInstance().time
        return SimpleDateFormat("EEEE").format(dayOfWeek)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(theDate: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = formatter.parse(theDate)
        return SimpleDateFormat("HH:mm").format(date)
    }
}