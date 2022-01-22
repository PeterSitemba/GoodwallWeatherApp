package faba.app.goodwallweatherapp.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.*
import faba.app.goodwallweatherapp.R
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastDays
import faba.app.goodwallweatherapp.utils.SpanningLinearLayoutManager
import faba.app.goodwallweatherapp.utils.Status
import faba.app.goodwallweatherapp.view.adapters.ForecastAdapter
import faba.app.goodwallweatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.host_frag.*
import permissions.dispatcher.*
import kotlin.math.roundToInt

@RuntimePermissions
class HomeFragment : Fragment() {

    lateinit var latitude: String
    val weatherViewModel: WeatherViewModel by activityViewModels()
    var forecastList: MutableList<ForecastDays> = mutableListOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.host_frag, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = ForecastAdapter { forecastDays -> adapterOnClick(forecastDays) }

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity as AppCompatActivity)

        getLastLocationWithPermissionCheck()

        observeCurrentViewModel()
        observeForecastViewModel(listAdapter)



        rvForecast.layoutManager = SpanningLinearLayoutManager(
            activity,
            SpanningLinearLayoutManager.VERTICAL,
            false
        )
        rvForecast.adapter = listAdapter

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        onRequestPermissionsResult(requestCode, grantResults)
    }


      fun initWeather(lat: Double, lon: Double) {
          weatherViewModel.getCurrentWeather(
              lat,
              lon,
              "2a5ac244383461b7c2225b066ef65029"
          )
          weatherViewModel.getWeatherForecast(
              lat,
              lon,
              "2a5ac244383461b7c2225b066ef65029"
          )
      }



    private fun adapterOnClick(forecastDays: ForecastDays) {
        Log.e("HomeFrag", "Clicked ${forecastDays.dt_txt}")
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

                        initHeaderView(it)
                    }
                }
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun initHeaderView(weatherData: WeatherData) {

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

        txtName.text = weatherData.name
        txtTemp.text = "${weatherData.main.temp.roundToInt()}\u00B0"
        txtMinTemp.text = "${weatherData.main.temp_min.roundToInt()}\u00B0"
        txtCurrentTemp.text = "${weatherData.main.temp.roundToInt()}\u00B0"
        txtMaxTemp.text = "${weatherData.main.temp_max.roundToInt()}\u00B0"
    }


    private fun observeForecastViewModel(listAdapter: ForecastAdapter) {
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
                        forecastList = it!!.list.toMutableList().filter { forecastsDays ->
                            forecastsDays.dt_txt.contains("12:00:00")
                        }.toMutableList()

                        listAdapter.submitList(forecastList)
                    }
                }
            }

        })
    }


    @SuppressLint("MissingPermission")
    @NeedsPermission(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun getLastLocation() {
        if (isLocationEnabled()) {
            fusedLocationClient.lastLocation.addOnCompleteListener(activity as AppCompatActivity) { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    latitude = location.latitude.toString()
                    initWeather(location.latitude, location.longitude)

                }
            }
        } else {
            Toast.makeText(activity, "Turn on location", Toast.LENGTH_LONG)
                .show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity as AppCompatActivity)
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude.toString()
            initWeather(mLastLocation.latitude, mLastLocation.longitude)
        }
    }


    @OnShowRationale(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun showRationaleForLocation(request: PermissionRequest) {
        showRationaleDialog(R.string.permission_required_text, request)
    }

    @OnPermissionDenied(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun onLocationDenied() {
        Toast.makeText(activity, R.string.permission_location_denied, Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    @OnNeverAskAgain(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun onLocationNeverAskAgain() {
        Toast.makeText(activity, R.string.permission_location_never_ask_again, Toast.LENGTH_SHORT)
            .show()
        activity?.finish()
    }

    private fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        AlertDialog.Builder(activity as AppCompatActivity)
            .setPositiveButton(R.string.allow) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.deny) { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

}




