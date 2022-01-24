package faba.app.goodwallweatherapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import faba.app.goodwallweatherapp.utils.Status
import faba.app.goodwallweatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.weather_details_frag.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel : WeatherViewModel by viewModels()
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            this.setKeepOnScreenCondition {
                weatherViewModel.loading.value!!
            }
        }
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

    }
}