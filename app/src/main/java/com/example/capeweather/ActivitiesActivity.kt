package com.example.capeweather

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ActivitiesActivity : AppCompatActivity() {

    private lateinit var citySpinner: Spinner
    private lateinit var sunnyButton: Button
    private lateinit var cloudyButton: Button
    private lateinit var windyButton: Button
    private lateinit var rainyButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var bottomNavigation: BottomNavigationView  // ✅ Correctly declared

    private val cities = arrayOf("Cape Town", "Johannesburg", "Durban", "Pretoria")
    private val apiBaseUrl = "https://activities-api-s8eq.onrender.com/activities"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities)

        // ✅ Initialize all views
        citySpinner = findViewById(R.id.citySpinner)
        sunnyButton = findViewById(R.id.sunnyButton)
        cloudyButton = findViewById(R.id.cloudyButton)
        windyButton = findViewById(R.id.windyButton)
        rainyButton = findViewById(R.id.rainyButton)
        resultTextView = findViewById(R.id.resultTextView)
        bottomNavigation = findViewById(R.id.bottomNavigation) // ✅ Fixed

        // ✅ Spinner setup
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        // ✅ Weather buttons
        sunnyButton.setOnClickListener { fetchActivities("Sunny") }
        cloudyButton.setOnClickListener { fetchActivities("Cloudy") }
        windyButton.setOnClickListener { fetchActivities("Windy") }
        rainyButton.setOnClickListener { fetchActivities("Rainy") }

        // ✅ Bottom navigation setup (moved here)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_menu -> {
                    startActivity(Intent(this, MenuActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchActivities(weather: String) {
        val selectedCity = citySpinner.selectedItem.toString()
        val encodedCity = URLEncoder.encode(selectedCity, "UTF-8").replace("+", "%20")
        val encodedWeather = URLEncoder.encode(weather, "UTF-8").replace("+", "%20")
        val urlString = "$apiBaseUrl/$encodedCity/$encodedWeather"

        resultTextView.text = "Loading activities..."

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val responseCode = connection.responseCode
                val responseText = connection.inputStream.bufferedReader().readText()

                withContext(Dispatchers.Main) {
                    if (responseCode == 200) {
                        val activities = Regex("\"activities\":\\[(.*?)\\]").find(responseText)?.groups?.get(1)?.value
                        val cleanActivities = activities?.replace("\"", "")?.split(",")?.joinToString("\n") ?: "No activities found"
                        resultTextView.text = "Activities for $selectedCity ($weather):\n\n$cleanActivities"
                    } else {
                        resultTextView.text = "Failed to fetch activities. Response code: $responseCode"
                    }
                }

                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    resultTextView.text = "Error: ${e.message}"
                    Toast.makeText(this@ActivitiesActivity, "Failed to fetch activities", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}