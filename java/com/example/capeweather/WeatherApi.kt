package com.example.capeweather.api

import com.example.capeweather.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.capeweather.WeatherResponseCurrent

interface WeatherApi {

    // 1️⃣ Fetch by city name (for Search screen)
    // Current weather by city name
    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponseCurrent

    // 2️⃣ Fetch by coordinates (for location-based forecast)
    @GET("data/2.5/forecast")
    fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Call<WeatherResponse>
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponseCurrent
}
