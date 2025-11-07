// File: FavouritesActivityUnitTest.kt
package com.example.capeweather

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], manifest = Config.NONE)
class FavouritesActivityUnitTest {

    @Test
    fun activity_shouldInitializeViews() {
        val activity = Robolectric.buildActivity(FavouritesActivity::class.java)
            .setup()
            .get()

        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerFavourites)
        val progressBar = activity.findViewById<View>(R.id.progressBarFavourites)
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        val bottomNav = activity.findViewById<BottomNavigationView>(R.id.bottomNavigation)

        assertNotNull(recyclerView)
        assertNotNull(progressBar)
        assertNotNull(toolbar)
        assertNotNull(bottomNav)
    }
}
