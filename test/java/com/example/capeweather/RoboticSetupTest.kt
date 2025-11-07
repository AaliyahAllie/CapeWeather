package com.example.capeweather

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RobolectricSetupTest {

    @Test
    fun checkSetup() {
        println("✅ Robolectric is correctly set up!")
    }
}
