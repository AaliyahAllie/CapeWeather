package com.example.capeweather

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var firstNameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var tempSwitch: SwitchMaterial
    private lateinit var defaultCitySpinner: Spinner
    private lateinit var logoutBtn: Button
    private lateinit var saveSettingsBtn: Button
    private lateinit var bottomNav: BottomNavigationView

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE)

        // Bind views
        firstNameInput = findViewById(R.id.firstNameInput)
        lastNameInput = findViewById(R.id.lastNameInput)
        emailInput = findViewById(R.id.emailInput)
        tempSwitch = findViewById(R.id.tempSwitch)
        defaultCitySpinner = findViewById(R.id.defaultCitySpinner)
        logoutBtn = findViewById(R.id.logoutBtn)
        saveSettingsBtn = findViewById(R.id.saveSettingsBtn)
        bottomNav = findViewById(R.id.bottomNavigation)

        // Spinner setup
        val cities = listOf("Select a default city", "Cape Town", "Johannesburg", "Durban", "Pretoria")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
        defaultCitySpinner.adapter = adapter

        // Load saved preferences
        val isFahrenheit = sharedPreferences.getBoolean("tempUnitFahrenheit", false)
        val savedCity = sharedPreferences.getString("defaultCity", "Select a default city")

        tempSwitch.isChecked = isFahrenheit
        defaultCitySpinner.setSelection(cities.indexOf(savedCity))

        // Switch change listener
        tempSwitch.setOnCheckedChangeListener { _, isChecked ->
            val unit = if (isChecked) "°F" else "°C"
            Toast.makeText(this, "Temperature set to $unit", Toast.LENGTH_SHORT).show()
        }

        // Save button
        saveSettingsBtn.setOnClickListener {
            val selectedCity = defaultCitySpinner.selectedItem.toString()
            val editor = sharedPreferences.edit()
            editor.putBoolean("tempUnitFahrenheit", tempSwitch.isChecked)
            editor.putString("defaultCity", selectedCity)
            editor.apply()

            Toast.makeText(this, "Settings saved successfully!", Toast.LENGTH_SHORT).show()
        }

        // Logout button
        logoutBtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginFragmentActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Bottom navigation listener
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_menu -> {
                    startActivity(Intent(this, MenuActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }

                else -> false
            }
        }
    }
}
