package com.example.capeweather

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class SettingsActivity : BaseActivity() {

    private lateinit var notificationSwitch: Switch
    private lateinit var tempUnitSwitch: Switch
    private lateinit var locationSwitch: Switch
    private lateinit var soundSwitch: Switch
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var languageSpinner: Spinner
    private var currentLanguageCode = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load saved language before UI loads
        loadLocale()
        setContentView(R.layout.activity_settings)

        sharedPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        currentLanguageCode = sharedPrefs.getString("app_language", "en") ?: "en"

        // === BIND UI ELEMENTS ===
        notificationSwitch = findViewById(R.id.notificationSwitch)
        tempUnitSwitch = findViewById(R.id.tempUnitSwitch)
        locationSwitch = findViewById(R.id.locationSwitch)
        soundSwitch = findViewById(R.id.soundSwitch)
        languageSpinner = findViewById(R.id.languageSpinner)
        bottomNav = findViewById(R.id.bottomNavigation)

        requestNotificationPermission()

        // === TOOLBAR ===
        val toolbar = findViewById<Toolbar>(R.id.settingsToolbar)
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener { finish() }

        setupBottomNavigation()

        // === LANGUAGE SPINNER ===
        val languages = resources.getStringArray(R.array.languages_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.setSelection(
            when (currentLanguageCode) {
                "af" -> 1
                "xh" -> 2
                else -> 0
            }
        )

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedCode = when (position) {
                    1 -> "af"
                    2 -> "xh"
                    else -> "en"
                }
                if (selectedCode != currentLanguageCode) {
                    currentLanguageCode = selectedCode
                    setLocale(selectedCode)
                    finish()
                    startActivity(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // === SWITCHES SETUP ===
        notificationSwitch.isChecked = sharedPrefs.getBoolean("notifications", true)
        tempUnitSwitch.isChecked = sharedPrefs.getBoolean("temp_unit_celsius", true)
        locationSwitch.isChecked = sharedPrefs.getBoolean("location_access", true)
        soundSwitch.isChecked = sharedPrefs.getBoolean("sound_vibration", true)

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("notifications", isChecked).apply()
        }

        tempUnitSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("temp_unit_celsius", isChecked).apply()
            if (isChecked) showNotification("Weather App", "Temperature unit set to Celsius")
            else showNotification("Weather App", "Temperature unit set to Fahrenheit")
        }

        locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("location_access", isChecked).apply()
        }

        soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("sound_vibration", isChecked).apply()
        }
    }

    // === BOTTOM NAVIGATION ===
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
                R.id.nav_settings -> true
                else -> false
            }
        }
    }

    // === LANGUAGE SUPPORT ===
    private fun setLocale(languageCode: String) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit().putString("app_language", languageCode).apply()

        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun loadLocale() {
        val langCode = getSharedPreferences("user_prefs", MODE_PRIVATE)
            .getString("app_language", "en") ?: "en"
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    // === NOTIFICATIONS ===
    private fun showNotification(title: String, message: String) {
        val channelId = "settings_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Settings Notifications", NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        NotificationManagerCompat.from(this)
            .notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }
}