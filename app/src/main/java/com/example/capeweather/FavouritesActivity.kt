package com.example.capeweather

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Intent

class FavouritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: View
    private lateinit var repo: WeatherRepository
    private lateinit var favManager: FavouriteCitiesManager
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        recyclerView = findViewById(R.id.recyclerFavourites)
        progressBar = findViewById(R.id.progressBarFavourites)
        bottomNav = findViewById(R.id.bottomNavigation)
        toolbar = findViewById(R.id.toolbar)
        sharedPrefs = getSharedPreferences("UserSettings", MODE_PRIVATE)

        repo = WeatherRepository()
        favManager = FavouriteCitiesManager(this)

        setupToolbar()
        setupBottomNavigation()
        loadFavourites()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            finish()
        }
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
            val favourites = favManager.getFavourites().toList()
            val isFahrenheit = sharedPrefs.getBoolean("tempUnitFahrenheit", false) // Get setting
            val weatherSummaries = favourites.map { city ->
                try {
                    val weather = repo.getWeatherByCityName(city)
                    val tempC = weather.main.temp
                    val temp = if (isFahrenheit) celsiusToFahrenheit(tempC) else tempC
                    val unit = if (isFahrenheit) "°F" else "°C"
                    val condition = weather.weather.firstOrNull()?.description ?: "N/A"
                    "${weather.name} - ${temp.toInt()}$unit - $condition"
                } catch (e: Exception) {
                    "$city - Error loading"
                }
            }

            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                recyclerView.layoutManager = LinearLayoutManager(this@FavouritesActivity)
                recyclerView.adapter = FavouritesAdapter(favourites, weatherSummaries) { city ->
                    // Delete city callback
                    AlertDialog.Builder(this@FavouritesActivity)
                        .setTitle("Delete Favourite")
                        .setMessage("Do you want to remove $city from favourites?")
                        .setPositiveButton("Yes") { _, _ ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                favManager.removeFavourite(city)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@FavouritesActivity,
                                        "$city removed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loadFavourites()
                                }
                            }
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        }
    }

    private fun celsiusToFahrenheit(celsius: Double): Double {
        return celsius * 9 / 5 + 32
    }

    // --- RecyclerView Adapter ---
    class FavouritesAdapter(
        private val cities: List<String>,
        private val summaries: List<String>,
        private val onDelete: (String) -> Unit
    ) : RecyclerView.Adapter<FavouritesAdapter.FavViewHolder>() {

        inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return FavViewHolder(view)
        }

        override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
            holder.textView.text = summaries[position]

            holder.itemView.setOnLongClickListener {
                onDelete(cities[position])
                true
            }
        }

        override fun getItemCount(): Int = cities.size
    }
}
