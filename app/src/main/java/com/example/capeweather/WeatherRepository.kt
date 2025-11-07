package com.example.capeweather

import com.example.capeweather.api.WeatherApi
import com.example.capeweather.WeatherResponseCurrent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context

class WeatherRepository(private val context: Context) { // pass context here

    private val API_KEY = "c301bd04d842ca67ef143f184bc11913"
    private val BASE_URL = "https://api.openweathermap.org/"

    private val api: WeatherApi by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    suspend fun getWeatherByCityName(city: String): WeatherResponseCurrent {
        return if (context.isOnline()) {
            try {
                val weather = api.getCurrentWeather(city, API_KEY, "metric")
                WeatherCache.saveCurrentWeather(context, weather) // Save to cache
                weather
            } catch (e: Exception) {
                // Fallback to cached version if API fails
                WeatherCache.loadCurrentWeather(context) ?: throw e
            }
        } else {
            // Offline → return cached weather
            WeatherCache.loadCurrentWeather(context)
                ?: throw Exception("No internet and no cached data available")
        }
    }
}
