package faba.app.goodwallweatherapp.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import faba.app.goodwallweatherapp.database.WeatherDao
import faba.app.goodwallweatherapp.database.WeatherRoomDatabase
import faba.app.goodwallweatherapp.repository.WeatherRepository
import faba.app.goodwallweatherapp.service.NetworkConnectionInterceptor
import faba.app.goodwallweatherapp.service.RetrofitService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private var httpClient: OkHttpClient? = null
    private val TIMEOUT_IN_SECONDS: Long = 60

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, @ApplicationContext appContext: Context): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/weather/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(getOkhttpClient(appContext)!!)
        .build()

    @Provides
    fun getOkhttpClient(@ApplicationContext appContext: Context): OkHttpClient? {
        httpClient?.let {
            return it
        } ?: kotlin.run {
            httpClient = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .callTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(NetworkConnectionInterceptor(appContext))
                .build()

        }
        return httpClient
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideWeatherForecastService(retrofit: Retrofit): RetrofitService =
        retrofit.create(RetrofitService::class.java)

    @Singleton
    @Provides
    fun provideRepository(retrofitService: RetrofitService, weatherDao : WeatherDao) =
        WeatherRepository(retrofitService, weatherDao)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): WeatherRoomDatabase =
        Room.databaseBuilder(
            appContext,
            WeatherRoomDatabase::class.java,
            "weather_application"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideWeatherDao(db: WeatherRoomDatabase) = db.WeatherDao()

}