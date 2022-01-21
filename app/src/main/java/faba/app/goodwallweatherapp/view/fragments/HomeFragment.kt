package faba.app.goodwallweatherapp.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import faba.app.goodwallweatherapp.R
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastData
import faba.app.goodwallweatherapp.models.forecast.ForecastDays
import faba.app.goodwallweatherapp.utils.Status
import faba.app.goodwallweatherapp.view.adapters.ForecastAdapter
import faba.app.goodwallweatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.host_frag.*
import kotlin.math.roundToInt
import android.view.WindowManager


class HomeFragment : Fragment() {

    val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var listAdapter: ForecastAdapter
    var forecastList: MutableList<ForecastDays> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.host_frag, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherViewModel.getCurrentWeather(
            -1.3761848,
            36.676677,
            "2a5ac244383461b7c2225b066ef65029"
        )
        weatherViewModel.getWeatherForecast(
            -1.3761848,
            36.676677,
            "2a5ac244383461b7c2225b066ef65029"
        )

        activity?.let { observeCurrentViewModel(it) }
        context?.let { observeForecastViewModel(it) }

        rvForecast.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun observeCurrentViewModel(context: Context) {
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

                        initHeaderView(it, context)
                    }
                }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun initHeaderView(weatherData: WeatherData, context: Context) {

        val window: Window = activity?.window!!
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        when (weatherData.weather[0].main) {
            "Clear" -> {
                window.statusBarColor = this.resources.getColor(R.color.sunny_header)
                cLHeader.background = resources.getDrawable(R.drawable.sunny)
                cLBody.setBackgroundColor(resources.getColor(R.color.sunny))
                txtWeather.text = "SUNNY"

            }
            "Clouds" -> {
                window.statusBarColor = this.resources.getColor(R.color.cloudy)
                cLHeader.background = resources.getDrawable(R.drawable.cloudy)
                cLBody.setBackgroundColor(resources.getColor(R.color.cloudy))
                txtWeather.text = "CLOUDY"


            }
            "Rain" -> {
                window.statusBarColor = this.resources.getColor(R.color.rainy)
                cLHeader.background = resources.getDrawable(R.drawable.rainy)
                cLBody.setBackgroundColor(resources.getColor(R.color.rainy))
                txtWeather.text = "RAIN"


            }
            "Snow" -> {
                window.statusBarColor = this.resources.getColor(R.color.rainy)
                cLHeader.background = resources.getDrawable(R.drawable.snowy)
                cLBody.setBackgroundColor(resources.getColor(R.color.rainy))
                txtWeather.text = "SNOW"


            }
        }

        txtTemp.text = "${weatherData.main.temp.roundToInt()}\u00B0"

        txtMinTemp.text = "${weatherData.main.temp_min.roundToInt()}\u00B0"
        txtCurrentTemp.text = "${weatherData.main.temp.roundToInt()}\u00B0"
        txtMaxTemp.text = "${weatherData.main.temp_max.roundToInt()}\u00B0"
    }


    private fun observeForecastViewModel(context: Context) {
        weatherViewModel.forecastWeather.observe(this, { response ->
            when (response.status) {
                Status.LOADING -> {
                    Log.e("MainActivity", "Loading...")
                }
                Status.ERROR -> {
                    Log.e("MainActivity", "Error!!!")
                }
                else -> {
                    response.data.let {
                        forecastList = it!!.list.toMutableList()

                        forecastList.filter { forecastsDays ->
                            forecastsDays.dt_txt.contains("12:00:00")
                        }

                        listAdapter =
                            ForecastAdapter(context, forecastList.filter { forecastsDays ->
                                forecastsDays.dt_txt.contains("12:00:00")
                            }.toMutableList())
                        rvForecast.adapter = listAdapter
                    }
                }
            }

        })
    }


}