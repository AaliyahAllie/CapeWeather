package com.example.capeweather

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.capeweather.model.WeatherResponse

class HomePagerAdapter(fragment: Fragment, private val weather: WeatherResponse) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) WeeklyForecastFragment(weather.daily)
        else TodaysPredictionFragment(weather.current)
    }
}
