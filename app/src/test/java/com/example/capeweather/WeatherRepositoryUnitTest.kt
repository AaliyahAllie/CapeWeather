// WeatherRepositoryUnitTest.kt
package com.example.capeweather

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherRepositoryUnitTest {

    private val repository = WeatherRepository()

    @Test
    fun `getWeatherByCityName returns non-null result`() = runBlocking {
        // Use a real city, e.g., "Cape Town"
        val result = repository.getWeatherByCityName("Cape Town")

        // Basic assertions
        assertTrue(result.name.isNotEmpty())
        assertTrue(result.main.temp >= -50) // just a sanity check
        assertTrue(result.weather.isNotEmpty())
    }
}
