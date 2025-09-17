package com.example.capeweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capeweather.model.WeatherResponse
import com.example.capeweather.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var cityName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var weatherDesc: TextView
    private lateinit var highLowTemp: TextView

    private val apiKey = "YOUR_API_KEY" // Replace with your OpenWeatherMap API key

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        cityName = view.findViewById(R.id.cityName)
        currentTemp = view.findViewById(R.id.currentTemp)
        weatherDesc = view.findViewById(R.id.weatherDesc)
        highLowTemp = view.findViewById(R.id.highLowTemp)

        fetchWeather("Cape Town")

        return view
    }

    private fun fetchWeather(city: String) {
        RetrofitClient.instance.getWeather(city, apiKey)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val weather = response.body()!!
                        cityName.text = city
                        currentTemp.text = "${weather.main.temp.toInt()}°"
                        weatherDesc.text = weather.weather[0].description.replaceFirstChar { it.uppercase() }
                        highLowTemp.text = "H:${weather.main.temp_max.toInt()}°  L:${weather.main.temp_min.toInt()}°"
                    } else {
                        Toast.makeText(requireContext(), "Failed to get weather data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error fetching weather: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
