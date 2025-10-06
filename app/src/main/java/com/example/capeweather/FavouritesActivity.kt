package com.example.capeweather

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var repo: WeatherRepository
    private lateinit var favManager: FavouriteCitiesManager
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        listView = findViewById(R.id.listFavourites)
        progressBar = findViewById(R.id.progressBarFavourites)
        bottomNav = findViewById(R.id.bottomNavigation)

        repo = WeatherRepository()
        favManager = FavouriteCitiesManager(this)

        setupBottomNavigation()
        loadFavourites()
    }

    private fun setupBottomNavigation() {
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

    private fun loadFavourites() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val favourites = favManager.getFavourites()
            val weatherSummaries = favourites.mapNotNull { city ->
                try {
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
                listView.adapter = ArrayAdapter(
                    this@FavouritesActivity,
                    android.R.layout.simple_list_item_1,
                    weatherSummaries
                )
            }
        }
    }
}
