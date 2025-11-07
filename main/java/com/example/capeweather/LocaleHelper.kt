package com.example.capeweather

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LocaleHelper {
    // Apply and persist the app language (Android 7+ via AppCompat)
    fun apply(languageTag: String) {
        val locales = LocaleListCompat.forLanguageTags(languageTag) // e.g. "en-ZA", "af", "xh"
        AppCompatDelegate.setApplicationLocales(locales)
        // AppCompat persists this automatically; no manual SharedPreferences needed
    }

    fun currentTag(): String {
        val list = AppCompatDelegate.getApplicationLocales()
        return list.toLanguageTags().ifEmpty { "en-ZA" }
    }
}
