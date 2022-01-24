package faba.app.goodwallweatherapp.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import faba.app.goodwallweatherapp.R
import faba.app.goodwallweatherapp.utils.DateUtil
import faba.app.goodwallweatherapp.view.animations.CascadingAnimatedFragment
import faba.app.goodwallweatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.list_forecast.view.*
import kotlinx.android.synthetic.main.weather_details_frag.*
import kotlin.math.roundToInt

class WeatherDetailsFrag : CascadingAnimatedFragment() {

    val weatherViewModel: WeatherViewModel by activityViewModels()


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
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.white)

        icBackButton.setOnClickListener {
            navController.navigateUp()
        }

        weatherViewModel.selectedForecast.observe(viewLifecycleOwner, { forecastDay ->
            val windSpeed = forecastDay.wind.speed * 3.6
            val weather = forecastDay.weather[0].main
            var temp =
                "${forecastDay.main.temp.roundToInt()}${resources.getString(R.string.degree)}"

            var day = DateUtil.getDay(forecastDay.dt_txt)
            if (day == DateUtil.getDayOfWeek()) {
                day = "Today"
                temp = weatherViewModel.currentTempForecast.value!!
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
                }
                "Clouds" -> {
                    txtSlogan.text = resources.getString(R.string.cloudy)

                }
                "Rain" -> {
                    txtSlogan.text = resources.getString(R.string.rainy)

                }
                "Snow" -> {
                    txtSlogan.text = resources.getString(R.string.snowy)

                }
            }


        })

        introAnimator.start()

    }

    override fun setIntroAnimator() {
        introAnimator.addViews(
            linearLayoutWeather after 100.milliseconds
        )
    }
}