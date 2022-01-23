package faba.app.goodwallweatherapp.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import faba.app.goodwallweatherapp.R
import kotlinx.android.synthetic.main.weather_details_frag.*

class WeatherDetailsFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.weather_details_frag, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window = activity?.window!!
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.white)

        icBackButton.setOnClickListener {
            navController.navigateUp()
        }

    }
}