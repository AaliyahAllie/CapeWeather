package com.example.capeweather

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var activitiesBtn: Button
    private lateinit var profileBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Initialize buttons
        activitiesBtn = findViewById(R.id.activitiesBtn)
        profileBtn = findViewById(R.id.profileBtn)

        // Navigate to ActivitiesActivity
        activitiesBtn.setOnClickListener {
            val intent = Intent(this, ActivitiesActivity::class.java)
            startActivity(intent)
        }

        // Navigate to ProfileActivity
        profileBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
