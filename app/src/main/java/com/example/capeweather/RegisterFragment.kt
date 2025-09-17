package com.example.capeweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val firstName = view.findViewById<EditText>(R.id.firstNameInput)
        val lastName = view.findViewById<EditText>(R.id.lastNameInput)
        val email = view.findViewById<EditText>(R.id.emailRegisterInput)
        val dob = view.findViewById<EditText>(R.id.dobInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput) // 👈 new
        val citySpinner = view.findViewById<Spinner>(R.id.citySpinner)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val googleSignIn = view.findViewById<LinearLayout>(R.id.googleSignInCustom)

        val cities = arrayOf("Cape Town", "Johannesburg", "Durban", "Pretoria")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        registerButton.setOnClickListener {
            val name = firstName.text.toString()
            val surname = lastName.text.toString()
            val emailText = email.text.toString()
            val dobText = dob.text.toString()
            val passwordText = passwordInput.text.toString() // 👈 user-provided
            val city = citySpinner.selectedItem.toString()

            if (name.isEmpty() || surname.isEmpty() || emailText.isEmpty() || dobText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create user in Firebase Auth
            auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid ?: ""
                        val user = User(name, surname, emailText, dobText, city)

                        // Save in database
                        database.getReference("users").child(uid).setValue(user)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "User registered successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Database error: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(requireContext(), "Auth failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        googleSignIn.setOnClickListener {
            Toast.makeText(requireContext(), "Google Sign-In clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
