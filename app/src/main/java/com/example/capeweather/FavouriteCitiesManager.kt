package com.example.capeweather

import android.content.Context
import android.content.SharedPreferences

class FavouriteCitiesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("favourite_cities", Context.MODE_PRIVATE)

    fun addFavourite(city: String) {
        val set = prefs.getStringSet("cities", mutableSetOf()) ?: mutableSetOf()
        set.add(city)
        prefs.edit().putStringSet("cities", set).apply()
    }

    fun getFavourites(): Set<String> {
        return prefs.getStringSet("cities", mutableSetOf()) ?: mutableSetOf()
    }

    fun removeFavourite(city: String) {
        val set = prefs.getStringSet("cities", mutableSetOf()) ?: mutableSetOf()
        set.remove(city)
        prefs.edit().putStringSet("cities", set).apply()
    }
}
