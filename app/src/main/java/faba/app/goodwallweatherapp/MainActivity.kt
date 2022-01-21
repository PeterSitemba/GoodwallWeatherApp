package faba.app.goodwallweatherapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import faba.app.goodwallweatherapp.utils.Status
import faba.app.goodwallweatherapp.viewmodel.WeatherViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel : WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //weatherViewModel.getCurrentWeather(-1.3761848, 36.676677, "2a5ac244383461b7c2225b066ef65029")
        //weatherViewModel.getWeatherForecast(-1.3761848, 36.676677, "2a5ac244383461b7c2225b066ef65029")

        //observeViewModel()
    }

    private fun observeViewModel() {
        weatherViewModel.currentWeather.observe(this, { response ->
            when (response.status) {
                Status.LOADING -> {
                    Log.e("MainActivity", "Loading...")
                }
                Status.ERROR -> {
                    Log.e("MainActivity", "Error!!!")
                }
                else -> {
                    response.data.let {
                        Log.e("MainActivity", it!!.main.temp.toString())
                    }
                }
            }

        })
    }
}