package com.example.capeweather

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.capeweather.api.WeatherApi
import com.example.capeweather.model.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class HomePageActivity : AppCompatActivity() {

    private lateinit var tempTv: TextView
    private lateinit var descriptionTv: TextView
    private lateinit var pressureTv: TextView
    private lateinit var windTv: TextView
    private lateinit var humidityTv: TextView
    private lateinit var visibilityTv: TextView
    private lateinit var sunriseTv: TextView
    private lateinit var sunsetTv: TextView
    private lateinit var citySpinner: Spinner
    private lateinit var bottomNav: BottomNavigationView

    private val apiKey = "c301bd04d842ca67ef143f184bc11913"

    private val cities = mapOf(
        "Cape Town" to Pair(-33.9249, 18.4241),
        "Pretoria" to Pair(-25.7479, 28.2293),
        "Johannesburg" to Pair(-26.2041, 28.0473),
        "Durban" to Pair(-29.8587, 31.0218)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Bind UI
        tempTv = findViewById(R.id.tempTv)
        descriptionTv = findViewById(R.id.descriptionTv)
        pressureTv = findViewById(R.id.pressureTv)
        windTv = findViewById(R.id.windTv)
        humidityTv = findViewById(R.id.humidityTv)
        visibilityTv = findViewById(R.id.visibilityTv)
        sunriseTv = findViewById(R.id.sunriseTv)
        sunsetTv = findViewById(R.id.sunsetTv)
        citySpinner = findViewById(R.id.citySpinner)
        bottomNav = findViewById(R.id.bottomNavigation)

        // Spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            cities.keys.toList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = parent?.getItemAtPosition(position).toString()
                val coords = cities[selectedCity]
                coords?.let { fetchWeather(it.first, it.second) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        citySpinner.setSelection(0)

        // Bottom Nav
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_search -> { Toast.makeText(this,"Coming soon",Toast.LENGTH_SHORT).show(); true }
                R.id.nav_settings -> { Toast.makeText(this,"Settings clicked",Toast.LENGTH_SHORT).show(); true }
                R.id.nav_menu -> { Toast.makeText(this,"Weekly Activities and others soon",Toast.LENGTH_SHORT).show(); true }
                else -> false
            }
        }
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherApi::class.java)
        val call = weatherApi.getForecast(lat, lon, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.list?.firstOrNull()?.let { item ->

                        // Main temperature + description
                        tempTv.text = "${item.main.temp}°C"
                        descriptionTv.text = item.weather[0].description.capitalize()

                        // Cards
                        pressureTv.text = "${item.main.pressure} hPa"
                        windTv.text = "${item.wind.speed} m/s"
                        humidityTv.text = "${item.main.humidity}%"
                        visibilityTv.text = "${item.visibility} m"

                        // Convert sunrise & sunset
                        val sunriseTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                            .format(Date(weatherResponse.city.sunrise * 1000L))
                        val sunsetTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                            .format(Date(weatherResponse.city.sunset * 1000L))

                        sunriseTv.text = sunriseTime
                        sunsetTv.text = sunsetTime
                    }
                } else {
                    Toast.makeText(this@HomePageActivity, "Failed to get weather", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@HomePageActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
