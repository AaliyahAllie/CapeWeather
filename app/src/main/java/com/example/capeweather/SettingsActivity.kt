package com.example.capeweather

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var switchTemperatureUnit: Switch
    private lateinit var switchNotifications: Switch
    private lateinit var switchLocationAccess: Switch
    private lateinit var switchSoundVibration: Switch
    private lateinit var languageSpinner: Spinner

    private lateinit var sharedPreferences: SharedPreferences

    private val CHANNEL_ID = "settings_notifications"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("UserSettings", Context.MODE_PRIVATE)

        createNotificationChannel()

        // Bind UI components
        switchTemperatureUnit = findViewById(R.id.switchTemperatureUnit)
        switchNotifications = findViewById(R.id.switchNotifications)
        switchLocationAccess = findViewById(R.id.switchLocationAccess)
        switchSoundVibration = findViewById(R.id.switchSoundVibration)
        languageSpinner = findViewById(R.id.languageSpinner)

        // Toolbar back button
        val toolbar = findViewById<Toolbar>(R.id.settingsToolbar)
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener { finish() }

        // Populate languages
        val languages = arrayOf("English", "Afrikaans", "Xhosa")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        // Load saved settings
        loadSettings()

        // Switch listeners
        switchTemperatureUnit.setOnCheckedChangeListener { _, isChecked ->
            val unit = if (isChecked) getString(R.string.celsius) else getString(R.string.fahrenheit)
            saveSetting("TempUnit", unit)
            showNotification(getString(R.string.temperature_unit_switch), getString(R.string.switched_to, unit))
        }

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("NotificationsEnabled", isChecked.toString())
        }

        switchLocationAccess.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("LocationAccess", isChecked.toString())
        }

        switchSoundVibration.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("SoundVibration", isChecked.toString())
        }

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val lang = languages[position]
                saveSetting("Language", lang)
                showNotification(getString(R.string.select_language), getString(R.string.switched_to, lang))
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun saveSetting(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun loadSettings() {
        val tempUnit = sharedPreferences.getString("TempUnit", getString(R.string.celsius))
        val notifications = sharedPreferences.getString("NotificationsEnabled", "true")
        val location = sharedPreferences.getString("LocationAccess", "false")
        val sound = sharedPreferences.getString("SoundVibration", "true")
        val language = sharedPreferences.getString("Language", "English")

        switchTemperatureUnit.isChecked = (tempUnit == getString(R.string.celsius))
        switchNotifications.isChecked = (notifications == "true")
        switchLocationAccess.isChecked = (location == "true")
        switchSoundVibration.isChecked = (sound == "true")

        languageSpinner.setSelection(
            when (language) {
                "Afrikaans" -> 1
                "Xhosa" -> 2
                else -> 0
            }
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Settings Notifications"
            val descriptionText = "Notifies users when settings change"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
