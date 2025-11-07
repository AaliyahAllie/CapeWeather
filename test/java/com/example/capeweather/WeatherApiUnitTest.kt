// File: WeatherApiUnitTest.kt
package com.example.capeweather.api

import com.example.capeweather.WeatherResponseCurrent
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.junit.Assert.assertNotNull

class WeatherApiUnitTest {

    private val api: WeatherApi = mock()

    @Test
    fun `mock getWeatherByCity returns response`() = runBlocking {
        // Create a mock of WeatherResponseCurrent
        val mockedResponse: WeatherResponseCurrent = mock()

        // Stub the API call
        whenever(api.getWeatherByCity("Cape Town", "fakeApiKey")).thenReturn(mockedResponse)

        // Call the API
        val result = api.getWeatherByCity("Cape Town", "fakeApiKey")

        // Simple assertion
        assertNotNull(result)
    }
}
