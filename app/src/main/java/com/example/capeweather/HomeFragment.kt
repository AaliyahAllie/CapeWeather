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
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var cityName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var weatherDesc: TextView
    private lateinit var highLowTemp: TextView
    private lateinit var viewPager: ViewPager2

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
        viewPager = view.findViewById(R.id.viewPager)

        fetchWeather()

        return view
    }

    private fun fetchWeather() {
        val lat = -33.9249
        val lon = 18.4241

        RetrofitClient.instance.getWeather(lat, lon, apiKey)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val weather = response.body()!!

                        cityName.text = "Cape Town"
                        currentTemp.text = "${weather.current.temp.toInt()}°"
                        weatherDesc.text = weather.current.weather[0].description.replaceFirstChar { it.uppercase() }
                        highLowTemp.text = "H:${weather.daily[0].temp.max.toInt()}°  L:${weather.daily[0].temp.min.toInt()}°"

                        val adapter = HomePagerAdapter(this@HomeFragment, weather)
                        viewPager.adapter = adapter

                        TabLayoutMediator(view!!.findViewById(R.id.tabLayout), viewPager) { tab, position ->
                            tab.text = if (position == 0) "Weekly Forecast" else "Today's Predictions"
                        }.attach()
                    } else {
                        Toast.makeText(requireContext(), "Failed to get weather data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
