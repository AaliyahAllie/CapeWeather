package com.example.capeweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capeweather.model.Daily

class WeeklyForecastFragment(private val dailyList: List<Daily>) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewWeekly)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = WeeklyAdapter(dailyList)
        recyclerView.adapter = adapter

        return view
    }
}
