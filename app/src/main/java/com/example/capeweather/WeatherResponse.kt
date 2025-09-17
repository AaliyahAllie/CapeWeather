package com.example.capeweather.model

data class WeatherResponse(
    val current: Current,
    val daily: List<Daily>
)

data class Current(
    val temp: Double,
    val humidity: Int,
    val wind_speed: Double,
    val sunrise: Long,
    val sunset: Long,
    val weather: List<Weather>
)

data class Daily(
    val dt: Long,
    val temp: Temp,
    val weather: List<Weather>
)

data class Temp(
    val min: Double,
    val max: Double
)

data class Weather(
    val description: String
)
