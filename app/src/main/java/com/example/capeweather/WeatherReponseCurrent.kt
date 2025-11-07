package com.example.capeweather

data class WeatherResponseCurrent(
    val name: String,
    val main: MainCurrent,
    val weather: List<Weather>,
    val wind: Wind,
    val visibility: Int,  // NEW
    val sunrise: Long,    // NEW
    val sunset: Long      // NEW
)

data class MainCurrent(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double,
    val deg: Int
)
