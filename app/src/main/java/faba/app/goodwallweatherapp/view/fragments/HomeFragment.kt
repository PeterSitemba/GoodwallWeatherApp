package faba.app.goodwallweatherapp.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import faba.app.goodwallweatherapp.R
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.models.forecast.ForecastDays
import faba.app.goodwallweatherapp.service.NetworkConnectionInterceptor
import faba.app.goodwallweatherapp.utils.Constants
import faba.app.goodwallweatherapp.utils.SpanningLinearLayoutManager
import faba.app.goodwallweatherapp.utils.Status
import faba.app.goodwallweatherapp.utils.navigateTo
import faba.app.goodwallweatherapp.view.adapters.ForecastAdapter
import faba.app.goodwallweatherapp.view.animations.CascadingAnimatedFragment
import faba.app.goodwallweatherapp.view.animations.NavAnimations
import faba.app.goodwallweatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.host_frag.*
import kotlinx.coroutines.launch
import permissions.dispatcher.*
import kotlin.math.roundToInt

@RuntimePermissions
class HomeFragment : CascadingAnimatedFragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    var forecastList: MutableList<ForecastDays> = mutableListOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val listAdapter = ForecastAdapter { forecastDays -> adapterOnClick(forecastDays) }


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                if (NetworkConnectionInterceptor(activity as AppCompatActivity).isNetworkAvailable()) {
                    requestLocation()
                } else {
                    requestLocation()
                    weatherViewModel.loading.value = false
                }
            } else {
                activity?.finish()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.host_frag, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkConnectionAndDB()

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity as AppCompatActivity)

        introAnimator.start()

        rvForecast.layoutManager = SpanningLinearLayoutManager(
            activity,
            SpanningLinearLayoutManager.VERTICAL,
            false
        )
        rvForecast.adapter = listAdapter

        observeCurrentViewModel()
        observeForecastViewModel(listAdapter)

        bRefresh.setOnClickListener {
            checkConnectionAndDB()
        }

        swiperefresh.setOnRefreshListener {
            refresh()
        }

    }

    private fun checkConnectionAndDB() {
        lifecycleScope.launch {
            weatherViewModel.getRowCount()?.collect {
                if (it == 0 && !NetworkConnectionInterceptor(activity as AppCompatActivity).isNetworkAvailable()) {
                    weatherViewModel.loading.value = false
                    cLNoInternet.visibility = View.VISIBLE
                } else if (it!! > 0 && !NetworkConnectionInterceptor(activity as AppCompatActivity).isNetworkAvailable()) {
                    cLNoInternet.visibility = View.GONE
                    weatherViewModel.getAllNoInternet()
                } else {
                    requestLocation()
                    cLNoInternet.visibility = View.GONE
                }
            }
        }

    }

    override fun setIntroAnimator() {
        introAnimator.addViews(
            rvForecast after 200.milliseconds,
            txtName after 100.milliseconds,
            txtTemp after 100.milliseconds,
            txtWeather after 100.milliseconds
        )
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
            Constants.API_KEY
        )
        weatherViewModel.getWeatherForecast(
            lat,
            lon,
            Constants.API_KEY
        )
    }


    private fun adapterOnClick(forecastDays: ForecastDays) {

        weatherViewModel.selectForecast(forecastDays)
        weatherViewModel.fullForecastList.value =
            weatherViewModel.fullForecastList.value?.filter { forecastsDays ->
                forecastsDays.dt_txt.contains(forecastDays.dt_txt.substring(0, 11))
            }?.toMutableList()

        navController.navigateTo(
            R.id.action_homeFragment_to_weatherDetailsFrag,
            animationsOverride = NavAnimations.DEFAULT
        )

    }

    private fun refresh(){
        if(!NetworkConnectionInterceptor(activity as AppCompatActivity).isNetworkAvailable()){
            weatherViewModel.getAllNoInternet()
            Toast.makeText(activity, resources.getString(R.string.error_noConnection), Toast.LENGTH_SHORT).show()
            swiperefresh.isRefreshing = false
        }else{
            requestLocation()
        }
    }

    private fun observeCurrentViewModel() {
        weatherViewModel.currentWeather.observe(this, { response ->
            when (response.status) {
                Status.LOADING -> {
                    Log.e("MainActivity", "Loading...")
                }
                Status.ERROR -> {
                    Log.e("MainActivity", "Error!!!")
                    weatherViewModel.loading.value = false
                }
                else -> {
                    weatherViewModel.loading.value = false
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

        weatherViewModel.currentTempForecast.value = "${weatherData.main.temp.roundToInt()}\u00B0"
    }


    private fun observeForecastViewModel(listAdapter: ForecastAdapter) {
        weatherViewModel.forecastWeather.observe(this, { response ->
            when (response.status) {
                Status.LOADING -> {
                    Log.e("MainActivity", "Loading...")
                }
                Status.ERROR -> {
                    weatherViewModel.loading.value = false
                    Log.e("MainActivity", "Error!!!")
                    swiperefresh.isRefreshing = false
                }
                else -> {
                    swiperefresh.isRefreshing = false
                    weatherViewModel.loading.value = false

                    response.data.let {
                        forecastList = it!!.list.toMutableList().filter { forecastsDays ->
                            forecastsDays.dt_txt.contains("12:00:00")
                        }.toMutableList()

                        listAdapter.submitList(forecastList)
                        listAdapter.setTheCurrentTemp(weatherViewModel.currentTempForecast.value!!)
                        weatherViewModel.fullForecastList.value = it.list.toMutableList()
                    }
                }
            }

        })
    }

    private fun requestLocation() {

        val mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)

        val client: SettingsClient =
            LocationServices.getSettingsClient(activity as AppCompatActivity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            getLastLocationWithPermissionCheck()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    resultLauncher.launch(intentSenderRequest)

                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener(activity as AppCompatActivity) { task ->
            val location: Location? = task.result
            if (location == null) {
                requestNewLocationData()
            } else {
                initWeather(location.latitude, location.longitude)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }

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




