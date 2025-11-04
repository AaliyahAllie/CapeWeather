package com.example.capeweather

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    private lateinit var notificationSwitch: Switch
    private lateinit var tempUnitSwitch: Switch
    private lateinit var locationSwitch: Switch
    private lateinit var soundSwitch: Switch
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        requestNotificationPermission()

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

        // Bind bottom navigation
        bottomNav = findViewById(R.id.bottomNavigation)
        setupBottomNavigation()

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
            val unit = if (isChecked) "Celsius" else "Fahrenheit"
            showNotification("Temperature Unit Changed", "You switched to $unit.")
        }

        locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("location_access", isChecked).apply()
        }

        soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("sound_vibration", isChecked).apply()
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
                    // Already in Settings
                    true
                }
                else -> false
            }
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "temp_change_channel"

        // Create a notification channel (for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Temperature Unit Changes",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifies when user changes temperature unit"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Build and show the notification
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ContextCompat.checkSelfPermission(
                    this@SettingsActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1001, builder.build())
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }
}



