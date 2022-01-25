package faba.app.goodwallweatherapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.google.gson.GsonBuilder
import faba.app.goodwallweatherapp.database.WeatherDao
import faba.app.goodwallweatherapp.database.WeatherRoomDatabase
import faba.app.goodwallweatherapp.models.current.WeatherData
import faba.app.goodwallweatherapp.repository.FakeRepo
import faba.app.goodwallweatherapp.repository.WeatherRepository
import faba.app.goodwallweatherapp.service.RetrofitService
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


@RunWith(JUnit4::class)
class GetWeatherTest {

    private val server = MockWebServer()
    private lateinit var repository: WeatherRepository
    private lateinit var mockedResponse: String
    private lateinit var weatherDao: WeatherDao
    lateinit var weatherData: WeatherData


    private val gson = GsonBuilder()
        .setLenient()
        .create()


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun init() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }
        server.start(8000)

        val BASE_URL = server.url("/").toString()

        val okHttpClient = OkHttpClient
            .Builder()
            .build()
        val service = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build().create(RetrofitService::class.java)

        val context = mock(Context::class.java)
        fun db(appContext: Context): WeatherRoomDatabase =
            Room.databaseBuilder(
                appContext,
                WeatherRoomDatabase::class.java,
                "weather_application"
            ).fallbackToDestructiveMigration().build()

        weatherDao = db(context).WeatherDao()

        repository = WeatherRepository(service, weatherDao, context)
        weatherData = FakeRepo().currentWeatherData
    }

    @Test
    fun `test api success`() {

        server.enqueue(MockResponse().setResponseCode(200))
        val response = runBlocking {
            repository.getCurrentWeatherApiForTest(
                37.422,
                -122.084,
                "d6990e541893547aed9f73c3b678d072"
            )
        }

        response.toObservable().doOnNext {
            val weatherDataApi = WeatherData(
                1,
                it.base,
                it.clouds,
                it.cod,
                it.coord,
                it.dt,
                it.main,
                it.name,
                it.sys,
                it.timezone,
                it.visibility,
                it.weather,
                it.wind
            )

            Assert.assertNotNull(response)
            Assert.assertTrue(weatherData == weatherDataApi)
        }


    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
        server.shutdown()

    }
}