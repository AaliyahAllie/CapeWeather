// src/test/java/com/example/capeweather/SettingsActivityLanguageTest.kt
package com.example.capeweather

import android.content.Context
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SettingsActivityLanguageTest {

    private lateinit var activity: SettingsActivity
    private lateinit var appCtx: Context

    @Before
    fun setUp() {
        // Start in English (South Africa) to match your default
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en-ZA"))

        activity = Robolectric.buildActivity(SettingsActivity::class.java).setup().get()
        appCtx = activity.applicationContext
    }

    @Test
    fun preselectsCurrentLocaleInSpinner() {
        val values = activity.resources.getStringArray(R.array.language_values) // ["en-ZA","af","xh"]
        val spinner = activity.findViewById<Spinner>(R.id.languageSpinner)

        val expectedIndex = values.indexOf("en-ZA").let { if (it >= 0) it else 0 }
        assertThat(spinner.selectedItemPosition, `is`(expectedIndex))
    }

    @Test
    fun selectingNewLanguage_appliesLocale_andPersistsToPrefs() {
        val spinner = activity.findViewById<Spinner>(R.id.languageSpinner)
        val values = activity.resources.getStringArray(R.array.language_values)

        val targetTag = "af"
        val targetIndex = values.indexOf(targetTag)
        require(targetIndex >= 0) { "Test expects 'af' in language_values." }

        activity.runOnUiThread {
            spinner.setSelection(targetIndex)
        }
        Shadows.shadowOf(activity.mainLooper).idle()

        // AppCompatDelegate locale applied
        val applied = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        assertThat(applied, `is`(targetTag))

        // Preference persisted
        val prefs = appCtx.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val persisted = prefs.getString("app_language_tag", null)
        assertThat(persisted, `is`(targetTag))
    }
}
