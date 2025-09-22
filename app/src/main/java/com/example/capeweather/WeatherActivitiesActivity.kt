// WeatherActivitiesActivity.kt
package com.example.capeweather

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

class WeatherActivitiesActivity : AppCompatActivity() {

    private val apiBaseUrl = "https://activities-api-s8eq.onrender.com/activities"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Root layout with gradient background
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            background = ContextCompat.getDrawable(
                this@WeatherActivitiesActivity,
                R.drawable.gradient_bg
            )
        }

        // Spinner for cities
        val citySpinner = Spinner(this)
        val cities = arrayOf("Cape Town", "Johannesburg", "Durban", "Pretoria")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter
        rootLayout.addView(citySpinner)

        // Buttons stacked vertically
        val weatherList = arrayOf("Sunny", "Cloudy", "Windy", "Rainy")
        weatherList.forEach { weather ->
            val button = Button(this).apply {
                text = weather
                setBackgroundColor(ContextCompat.getColor(this@WeatherActivitiesActivity, R.color.blue))
                setTextColor(ContextCompat.getColor(this@WeatherActivitiesActivity, android.R.color.white))
                setOnClickListener { fetchActivities(citySpinner.selectedItem.toString(), weather) }
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 16, 0, 0)
            button.layoutParams = params
            rootLayout.addView(button)
        }

        setContentView(rootLayout)
    }

    private fun fetchActivities(city: String, weather: String) {
        val encodedCity = URLEncoder.encode(city, "UTF-8").replace("+", "%20")
        val encodedWeather = URLEncoder.encode(weather, "UTF-8")
        val url = "$apiBaseUrl/$encodedCity/$encodedWeather"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { showPopup("Error", "Failed to fetch activities: ${e.message}") }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        runOnUiThread { showPopup("Error", "Error: ${it.code}") }
                    } else {
                        val json = JSONObject(it.body!!.string())
                        val activities = json.getJSONArray("activities")
                        val sb = StringBuilder()
                        for (i in 0 until activities.length()) {
                            sb.append("- ${activities.getString(i)}\n")
                        }
                        runOnUiThread { showPopup("$city - $weather", sb.toString()) }
                    }
                }
            }
        })
    }

    private fun showPopup(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
