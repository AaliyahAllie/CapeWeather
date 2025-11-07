// File: WeatherResponseUnitTest.kt
package com.example.capeweather.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WeatherResponseUnitTest {

    @Test
    fun `WeatherResponse data class should initialize correctly`() {
        // Create all nested objects
        val coord = Coord(lat = 33.9258, lon = 18.4232)
        val city = City(
            id = 1,
            name = "Cape Town",
            coord = coord,
            country = "ZA",
            population = 1000000,
            timezone = 7200,
            sunrise = 1696600000L,
            sunset = 1696640000L
        )

        val main = Main(
            temp = 20.0,
            feels_like = 19.5,
            temp_min = 18.0,
            temp_max = 22.0,
            pressure = 1013,
            humidity = 60
        )

        val weather = Weather(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        )

        val clouds = Clouds(all = 5)
        val wind = Wind(speed = 3.5, deg = 90, gust = 5.0)
        val rain = Rain(`3h` = 0.0)

        val weatherItem = WeatherItem(
            dt = 1696610000L,
            main = main,
            weather = listOf(weather),
            clouds = clouds,
            wind = wind,
            visibility = 10000,
            pop = 0.0,
            dt_txt = "2025-10-07 12:00:00",
            rain = rain
        )

        val response = WeatherResponse(
            cod = "200",
            message = 0,
            cnt = 1,
            list = listOf(weatherItem),
            city = city
        )

        // Assertions
        assertNotNull(response)
        assertEquals("200", response.cod)
        assertEquals(1, response.list.size)
        assertEquals("Cape Town", response.city.name)
        assertEquals(20.0, response.list[0].main.temp, 0.0)
        assertEquals("Clear", response.list[0].weather[0].main)
    }
}
