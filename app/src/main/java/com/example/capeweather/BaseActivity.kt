package com.example.capeweather

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Access the shared preferences where language is saved
        val prefs: SharedPreferences = newBase.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val langCode = prefs.getString("app_language", "en") ?: "en"

        // Set the new locale
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        // Apply the locale to configuration
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)

        // Create a new context with the updated configuration
        val context = newBase.createConfigurationContext(config)

        // Pass the localized context to the base activity
        super.attachBaseContext(context)
    }
}