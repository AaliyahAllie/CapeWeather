package com.example.capeweather

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    private lateinit var notificationSwitch: Switch
    private lateinit var tempUnitSwitch: Switch
    private lateinit var locationSwitch: Switch
    private lateinit var soundSwitch: Switch
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var homeBtn: Button  // Home button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Toolbar with back arrow
        val toolbar = findViewById<Toolbar>(R.id.settingsToolbar)
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener { finish() }

        // Initialize SharedPreferences
        sharedPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // Bind switches
        notificationSwitch = findViewById(R.id.notificationSwitch)
        tempUnitSwitch = findViewById(R.id.tempUnitSwitch)
        locationSwitch = findViewById(R.id.locationSwitch)
        soundSwitch = findViewById(R.id.soundSwitch)

        // Bind home button (new)
        homeBtn = findViewById(R.id.homeButton)
        homeBtn.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // Load saved preferences
        notificationSwitch.isChecked = sharedPrefs.getBoolean("notifications", true)
        tempUnitSwitch.isChecked = sharedPrefs.getBoolean("temp_unit_celsius", true)
        locationSwitch.isChecked = sharedPrefs.getBoolean("location_access", true)
        soundSwitch.isChecked = sharedPrefs.getBoolean("sound_vibration", true)

        // Switch listeners: save preferences on toggle
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("notifications", isChecked).apply()
        }

        tempUnitSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("temp_unit_celsius", isChecked).apply()
        }

        locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("location_access", isChecked).apply()
        }

        soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("sound_vibration", isChecked).apply()
        }
    }
}