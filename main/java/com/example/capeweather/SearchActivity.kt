package com.example.capeweather

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import androidx.appcompat.widget.Toolbar


class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var cityInput: EditText
    private lateinit var searchButton: Button
    private lateinit var resultText: TextView
    private lateinit var addFavButton: Button
    private lateinit var favButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialize UI components
        cityInput = findViewById(R.id.editCityName)
        searchButton = findViewById(R.id.btnSearchCity)
        resultText = findViewById(R.id.txtSearchResult)
        addFavButton = findViewById(R.id.btnAddFavourite)
        favButton = findViewById(R.id.btnViewFavourites)
        progressBar = findViewById(R.id.progressBarSearch)
        bottomNav = findViewById(R.id.bottomNavigation)

        setupBottomNavigation()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

// Handle back arrow click
        toolbar.setNavigationOnClickListener {
            finish()  // Go back to previous screen
        }

        searchButton.setOnClickListener {
            val city = cityInput.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Enter a city name", Toast.LENGTH_SHORT).show()
            } else {
                fetchWeather(city)
            }
        }

        addFavButton.setOnClickListener {
            viewModel.weatherResult?.let { weather ->
                FavouriteCitiesManager(this).addFavourite(weather.name)
                Toast.makeText(this, "${weather.name} added to favourites", Toast.LENGTH_SHORT).show()
            }
        }

        favButton.setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_menu -> {
                    startActivity(Intent(this, MenuActivity::class.java))
                    true
                }
                R.id.nav_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
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


    private fun fetchWeather(city: String) {
        progressBar.visibility = View.VISIBLE
        val repo = WeatherRepository()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = repo.getWeatherByCityName(city)
                viewModel.weatherResult = response
                displayWeather(response)
            } catch (e: Exception) {
                resultText.text = "Error fetching weather: ${e.message}"
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun displayWeather(weather: WeatherResponseCurrent) {
        val temp = weather.main.temp
        val condition = weather.weather.firstOrNull()?.description ?: "N/A"
        val windSpeed = weather.wind.speed
        val cityName = weather.name

        resultText.text = """
            🌍 City: $cityName
            🌡️ Temp: ${temp}°C
            ☁️ Condition: $condition
            💨 Wind: ${windSpeed} m/s
        """.trimIndent()
    }
}
