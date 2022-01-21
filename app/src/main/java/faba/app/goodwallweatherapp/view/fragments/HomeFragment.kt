package faba.app.goodwallweatherapp.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import faba.app.goodwallweatherapp.R
import faba.app.goodwallweatherapp.models.forecast.ForecastData
import faba.app.goodwallweatherapp.models.forecast.ForecastDays
import faba.app.goodwallweatherapp.utils.Status
import faba.app.goodwallweatherapp.view.adapters.ForecastAdapter
import faba.app.goodwallweatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.host_frag.*


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

        observeCurrentViewModel()
        context?.let { observeForecastViewModel(it) }

        rvForecast.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun observeCurrentViewModel() {
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