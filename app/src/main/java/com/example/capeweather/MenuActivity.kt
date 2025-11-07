package com.example.capeweather

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : BaseActivity() {

    private lateinit var activitiesBtn: Button
    private lateinit var profileBtn: Button
    private lateinit var favouritesBtn: Button
    private lateinit var homeBtn: Button
    private lateinit var settingsBtn: Button
    private lateinit var searchBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Initialize buttons
        activitiesBtn = findViewById(R.id.activitiesBtn)
        profileBtn = findViewById(R.id.profileBtn)
        favouritesBtn = findViewById(R.id.favouritesBtn)
        homeBtn = findViewById(R.id.homeBtn)
        settingsBtn = findViewById(R.id.settingsBtn)
        searchBtn = findViewById(R.id.searchBtn)

        // Navigate to ActivitiesActivity
        activitiesBtn.setOnClickListener {
            startActivity(Intent(this, ActivitiesActivity::class.java))
        }

        // Navigate to ProfileActivity
        profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Navigate to FavouritesActivity
        favouritesBtn.setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
        }

        // Navigate to HomePageActivity
        homeBtn.setOnClickListener {
            startActivity(Intent(this, HomePageActivity::class.java))
        }

        // Navigate to SettingsActivity
        settingsBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Navigate to SearchActivity
        searchBtn.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }
}
