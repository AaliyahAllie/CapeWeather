package com.example.capeweather

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.capeweather.model.WeatherResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageActivity : AppCompatActivity() {

    private lateinit var cityName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var highLowTemp: TextView
    private lateinit var tabWeekly: Button
    private lateinit var tabToday: Button
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Initialize UI components
        cityName = findViewById(R.id.cityName)
        currentTemp = findViewById(R.id.currentTemp)
        highLowTemp = findViewById(R.id.highLowTemp)
        tabWeekly = findViewById(R.id.tabWeekly)
        tabToday = findViewById(R.id.tabToday)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Default fragment: Weekly
        supportFragmentManager.commit {
            replace(R.id.tabContent, WeeklyFragment())
        }

        // Tab buttons
        tabWeekly.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.tabContent, WeeklyFragment())
            }
        }

        tabToday.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.tabContent, TodayFragment())
            }
        }

        // Bottom navigation
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    // TODO: Open Search Screen
                    true
                }
                R.id.nav_settings -> {
                    // TODO: Open Settings Screen
                    true
                }
                R.id.nav_menu -> {
                    // Open WeatherActivitiesActivity
                    val intent = Intent(this, WeatherActivitiesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Fetch weather for default city
        fetchWeather("Cape Town")
    }

    private fun fetchWeather(city: String) {
        val apiKey = "c301bd04d842ca67ef143f184bc11913"
        val call = WeatherApiClient.api.getCurrentWeather(city, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    weather?.let {
                        cityName.text = city
                        currentTemp.text = "${it.current.temp.toInt()}°"
                        highLowTemp.text = "H:${it.daily[0].temp.max.toInt()}° L:${it.daily[0].temp.min.toInt()}°"
                    }
                } else {
                    cityName.text = "Error: ${response.code()}"
                    currentTemp.text = "--°"
                    highLowTemp.text = "H:-- L:--"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                cityName.text = "Error loading"
                currentTemp.text = "--°"
                highLowTemp.text = "H:-- L:--"
            }
        })
    }
}
