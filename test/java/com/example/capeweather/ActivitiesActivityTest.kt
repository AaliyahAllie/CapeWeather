package com.example.capeweather

import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import androidx.appcompat.app.AppCompatDelegate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], manifest = Config.NONE)
class ActivitiesActivityUnitTest {

    private lateinit var activity: ActivitiesActivity

    @Before
    fun setup() {
        // Enable vector drawable compatibility
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        // Launch activity
        activity = Robolectric.buildActivity(ActivitiesActivity::class.java)
            .setup()
            .get()
    }

    @Test
    fun activity_shouldInitializeViews() {
        val spinner = activity.findViewById<Spinner>(R.id.citySpinner)
        val sunnyButton = activity.findViewById<Button>(R.id.sunnyButton)
        val cloudyButton = activity.findViewById<Button>(R.id.cloudyButton)
        val resultTextView = activity.findViewById<TextView>(R.id.resultTextView)
        val bottomNav = activity.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

        assertNotNull("Spinner should not be null", spinner)
        assertNotNull("Sunny button should not be null", sunnyButton)
        assertNotNull("Cloudy button should not be null", cloudyButton)
        assertNotNull("Result TextView should not be null", resultTextView)
        assertNotNull("BottomNavigationView should not be null", bottomNav)
        assertNotNull("Toolbar should not be null", toolbar)
    }

    @Test
    fun clickingSunnyButton_setsLoadingText() {
        val sunnyButton = activity.findViewById<Button>(R.id.sunnyButton)
        val resultTextView = activity.findViewById<TextView>(R.id.resultTextView)

        sunnyButton.performClick()
        assertEquals("Loading activities...", resultTextView.text.toString())
    }

    @Test
    fun clickingCloudyButton_setsLoadingText() {
        val cloudyButton = activity.findViewById<Button>(R.id.cloudyButton)
        val resultTextView = activity.findViewById<TextView>(R.id.resultTextView)

        cloudyButton.performClick()
        assertEquals("Loading activities...", resultTextView.text.toString())
    }
}
