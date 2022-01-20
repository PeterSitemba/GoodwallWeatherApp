package faba.app.goodwallweatherapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object DateUtil {

    @SuppressLint("SimpleDateFormat")
    fun getDay(theDate: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = formatter.parse(theDate)
        return SimpleDateFormat("EEEE").format(date)
    }
}