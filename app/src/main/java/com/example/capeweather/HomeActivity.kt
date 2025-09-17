package com.example.capeweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Load HomeFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFragmentContainer, HomeFragment())
            .commit()
    }
}
