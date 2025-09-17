package com.example.capeweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.capeweather.model.Current
import java.text.SimpleDateFormat
import java.util.*

class TodaysPredictionFragment(private val current: Current) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_todays_prediction, container, false)

        view.findViewById<TextView>(R.id.humidity).text = "Humidity: ${current.humidity}%"
        view.findViewById<TextView>(R.id.wind).text = "Wind: ${current.wind_speed} m/s"
        view.findViewById<TextView>(R.id.sunrise).text = "Sunrise: ${formatTime(current.sunrise)}"
        view.findViewById<TextView>(R.id.sunset).text = "Sunset: ${formatTime(current.sunset)}"

        return view
    }

    private fun formatTime(time: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(time * 1000))
    }
}
