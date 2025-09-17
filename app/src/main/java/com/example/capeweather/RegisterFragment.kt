package com.example.capeweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ✅ fixed layout name
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val firstName = view.findViewById<EditText>(R.id.firstNameInput)
        val lastName = view.findViewById<EditText>(R.id.lastNameInput)
        val email = view.findViewById<EditText>(R.id.emailRegisterInput)
        val dob = view.findViewById<EditText>(R.id.dobInput)
        val citySpinner = view.findViewById<Spinner>(R.id.citySpinner)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val googleSignIn = view.findViewById<LinearLayout>(R.id.googleSignInCustom)

        // Spinner with default cities
        val cities = arrayOf("Cape Town", "Johannesburg", "Durban", "Pretoria")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        // Register button click listener
        registerButton.setOnClickListener {
            val name = firstName.text.toString()
            val surname = lastName.text.toString()
            val emailText = email.text.toString()
            val dobText = dob.text.toString()
            val city = citySpinner.selectedItem.toString()

            if (name.isEmpty() || surname.isEmpty() || emailText.isEmpty() || dobText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Registered $name $surname from $city",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        googleSignIn.setOnClickListener {
            Toast.makeText(requireContext(), "Google Sign-In clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
