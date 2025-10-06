package com.example.capeweather

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var repo: WeatherRepository
    private lateinit var favManager: FavouriteCitiesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        listView = findViewById(R.id.listFavourites)
        progressBar = findViewById(R.id.progressBarFavourites)

        repo = WeatherRepository()
        favManager = FavouriteCitiesManager(this)

        loadFavourites()
    }

    private fun loadFavourites() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val favourites = favManager.getFavourites()
            val weatherSummaries = favourites.mapNotNull { city ->
                try {
                    // Fetch current weather for this city
                    val weather = repo.getWeatherByCityName(city)

                    val temp = weather.main.temp
                    val condition = weather.weather.firstOrNull()?.description ?: "N/A"
                    val cityName = weather.name

                    "$cityName - ${temp}°C - $condition"
                } catch (e: Exception) {
                    "$city - Error loading"
                }
            }

            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                val adapter = ArrayAdapter(this@FavouritesActivity,
                    android.R.layout.simple_list_item_1,
                    weatherSummaries)
                listView.adapter = adapter
            }
        }
    }
}
