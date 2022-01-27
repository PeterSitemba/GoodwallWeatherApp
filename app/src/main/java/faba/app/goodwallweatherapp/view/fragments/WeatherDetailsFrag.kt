package faba.app.goodwallweatherapp.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import faba.app.goodwallweatherapp.R
import faba.app.goodwallweatherapp.models.forecast.ForecastDays
import faba.app.goodwallweatherapp.utils.DateUtil
import faba.app.goodwallweatherapp.utils.navigateTo
import faba.app.goodwallweatherapp.view.adapters.DayForecastAdapter
import faba.app.goodwallweatherapp.view.animations.CascadingAnimatedFragment
import faba.app.goodwallweatherapp.view.animations.NavAnimations
import kotlinx.android.synthetic.main.weather_details_frag.*
import kotlin.math.roundToInt

class WeatherDetailsFrag : CascadingAnimatedFragment() {

    private val listAdapter = DayForecastAdapter { forecastDays -> adapterOnClick(forecastDays) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.weather_details_frag, container, false)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window = activity?.window!!
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity!!, R.color.white)

        icBackButton.setOnClickListener {
            navController.navigateUp()
        }

        rvDayForecast.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        rvDayForecast.adapter = listAdapter
        //observables
        initObservable()
        initDayForecast()

        introAnimator.start()

    }

    private fun initObservable() {
        viewModel.selectedForecast.observe(viewLifecycleOwner, { forecastDay ->
            val windSpeed = forecastDay.wind.speed * 3.6
            val weather = forecastDay.weather[0].main
            var temp =
                "${forecastDay.main.temp.roundToInt()}${resources.getString(R.string.degree)}"

            var day = DateUtil.getDay(forecastDay.dt_txt)
            if (day == DateUtil.getDayOfWeek()) {
                day = resources.getString(R.string.today)
                temp = viewModel.currentTempForecast.value!!
            }
            txtDayDetails.text = day
            txtTempDetails.text = temp

            txtWindSpeed.text = "${windSpeed.roundToInt()}${resources.getString(R.string.km)}"
            txtHumidity.text =
                "${forecastDay.main.humidity}${resources.getString(R.string.percent)}"
            txtWeatherDetails.text = weather

            when (weather) {
                "Clear" -> {
                    txtSlogan.text = resources.getString(R.string.clear)
                    ivDetailsBg.setImageResource(R.drawable.sunny_bg_small)
                }
                "Clouds" -> {
                    txtSlogan.text = resources.getString(R.string.cloudy)
                    ivDetailsBg.setImageResource(R.drawable.cloudy_bg_small)

                }
                "Rain" -> {
                    txtSlogan.text = resources.getString(R.string.rainy)
                    ivDetailsBg.setImageResource(R.drawable.rainy_bg_small)

                }
                "Snow" -> {
                    txtSlogan.text = resources.getString(R.string.snowy)
                    ivDetailsBg.setImageResource(R.drawable.snowy_bg_small)
                }
            }


        })

    }

    private fun initDayForecast() {
        listAdapter.submitList(viewModel.fullForecastList.value)
    }

    private fun adapterOnClick(forecastDays: ForecastDays) {

/*
        navController.navigateTo(
            R.id.action_homeFragment_to_weatherDetailsFrag,
            animationsOverride = NavAnimations.DEFAULT
        )
*/

    }


    override fun setIntroAnimator() {
        introAnimator.addViews(
            linearLayoutWeather after 100.milliseconds
        )
    }
}