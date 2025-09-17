package com.example.capeweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capeweather.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class WeeklyAdapter(private val dailyList: List<Daily>) : RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return WeeklyViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        val daily = dailyList[position]
        val date = SimpleDateFormat("EEE", Locale.getDefault()).format(Date(daily.dt * 1000))
        holder.day.text = date
        holder.temp.text = "H:${daily.temp.max.toInt()}°  L:${daily.temp.min.toInt()}°"
        holder.desc.text = daily.weather[0].description.replaceFirstChar { it.uppercase() }
    }

    override fun getItemCount() = dailyList.size

    class WeeklyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView = view.findViewById(R.id.day)
        val temp: TextView = view.findViewById(R.id.temp)
        val desc: TextView = view.findViewById(R.id.desc)
    }
}
