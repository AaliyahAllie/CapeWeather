package com.example.capeweather

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.capeweather.api.WeatherApi
import com.example.capeweather.model.WeatherResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import androidx.appcompat.app.AppCompatActivity

class HomePageActivity : BaseActivity() {

    private lateinit var sharedPrefs: SharedPreferences
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
    private lateinit var lastUpdatedTv: TextView

    private var showFahrenheit = false
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

        // Bind views
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
        lastUpdatedTv = findViewById(R.id.txtLastUpdated) // NEW TextView for timestamp

        sharedPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // Setup spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        // Spinner selection listener
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCity = parent?.getItemAtPosition(position).toString()
                val coords = cities[selectedCity]
                coords?.let { fetchWeather(it.first, it.second) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_menu -> {
                    startActivity(Intent(this, MenuActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.nav_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        showFahrenheit = sharedPrefs.getBoolean("tempUnitFahrenheit", false)
        val defaultCity = sharedPrefs.getString("defaultCity", "Cape Town") ?: "Cape Town"

        val currentSelection = citySpinner.selectedItem?.toString()
        if (currentSelection != defaultCity) {
            val position = (citySpinner.adapter as ArrayAdapter<String>).getPosition(defaultCity)
            citySpinner.setSelection(position)
        } else {
            cities[defaultCity]?.let { fetchWeather(it.first, it.second) }
        }
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        val useCelsius = sharedPrefs.getBoolean("temp_unit_celsius", true)
        val units = if (useCelsius) "metric" else "imperial"

        if (!isOnline()) {
            // Load cached weather if offline
            val cached = WeatherCache.loadCurrentWeather(this)
            if (cached != null) {
                displayWeather(cached, useCelsius)
                val lastUpdate = WeatherCache.getLastUpdateTime(this)
                lastUpdatedTv.text = "Last updated: ${formatTimestamp(lastUpdate)}"
                Toast.makeText(this, "Offline mode: showing last saved weather", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No internet & no cached data", Toast.LENGTH_LONG).show()
            }
            return
        }

        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherApi::class.java)
        val call = weatherApi.getForecast(lat, lon, apiKey, units)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.list?.firstOrNull()?.let { item ->
                        val currentWeather = WeatherResponseCurrent(
                            name = weatherResponse.city.name,
                            main = MainCurrent(
                                temp = item.main.temp,
                                feels_like = item.main.feels_like,
                                temp_min = item.main.temp_min,
                                temp_max = item.main.temp_max,
                                pressure = item.main.pressure,
                                humidity = item.main.humidity
                            ),
                            weather = item.weather.map { w ->
                                Weather(
                                    id = w.id,
                                    main = w.main,
                                    description = w.description,
                                    icon = w.icon
                                )
                            },
                            wind = Wind(speed = item.wind.speed, deg = item.wind.deg),
                            visibility = item.visibility,           // NEW
                            sunrise = weatherResponse.city.sunrise, // NEW
                            sunset = weatherResponse.city.sunset    // NEW
                        )
                        // Save cache
                        WeatherCache.saveCurrentWeather(this@HomePageActivity, currentWeather)
                        displayWeather(currentWeather, useCelsius)
                        lastUpdatedTv.text = "Last updated: ${formatTimestamp(System.currentTimeMillis())}"
                    }
                } else {
                    Toast.makeText(this@HomePageActivity, "Failed to get weather data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@HomePageActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayWeather(weather: WeatherResponseCurrent, useCelsius: Boolean) {
        val tempSymbol = if (useCelsius) "°C" else "°F"
        var temperature = weather.main.temp
        if (!useCelsius) {
            temperature = (temperature * 9 / 5) + 32
        }

        tempTv.text = String.format("%.1f%s", temperature, tempSymbol)
        descriptionTv.text = weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "N/A"
        pressureTv.text = "${weather.main.pressure} hPa"
        windTv.text = "${weather.wind.speed} m/s"
        humidityTv.text = "${weather.main.humidity}%"
        visibilityTv.text = "${weather.visibility / 1000.0} km"
        sunriseTv.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date(weather.sunrise * 1000))
        sunsetTv.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date(weather.sunset * 1000))
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun formatTimestamp(timeMillis: Long): String {
        val sdf = SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }
}