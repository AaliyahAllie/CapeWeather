package com.example.capeweather

import android.content.Context
import com.google.gson.Gson

object WeatherCache {

    private const val PREFS_NAME = "weather_cache"
    private const val KEY_CURRENT = "current_weather"

    fun saveCurrentWeather(context: Context, weather: WeatherResponseCurrent) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(weather)
        prefs.edit().putString(KEY_CURRENT, json).apply()
        // Optionally save timestamp
        prefs.edit().putLong("last_update", System.currentTimeMillis()).apply()
    }

    fun loadCurrentWeather(context: Context): WeatherResponseCurrent? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_CURRENT, null) ?: return null
        return Gson().fromJson(json, WeatherResponseCurrent::class.java)
    }

    fun getLastUpdateTime(context: Context): Long {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getLong("last_update", 0L)
    }
}