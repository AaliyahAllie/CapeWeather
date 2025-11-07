package com.example.capeweather

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthCredential
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val firstName = view.findViewById<EditText>(R.id.firstNameInput)
        val lastName = view.findViewById<EditText>(R.id.lastNameInput)
        val email = view.findViewById<EditText>(R.id.emailRegisterInput)
        val dob = view.findViewById<EditText>(R.id.dobInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val citySpinner = view.findViewById<Spinner>(R.id.citySpinner)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val googleSignInButton = view.findViewById<LinearLayout>(R.id.googleSignInCustom)

        val cities = arrayOf("Cape Town", "Johannesburg", "Durban", "Pretoria")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // --- Email/Password registration ---
        registerButton.setOnClickListener {
            val name = firstName.text.toString()
            val surname = lastName.text.toString()
            val emailText = email.text.toString()
            val dobText = dob.text.toString()
            val passwordText = passwordInput.text.toString()
            val city = citySpinner.selectedItem.toString()

            if (name.isEmpty() || surname.isEmpty() || emailText.isEmpty() || dobText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid ?: ""
                        val user = User(name, surname, emailText, dobText, city)
                        database.getReference("users").child(uid).setValue(user)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "User registered successfully!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(requireContext(), HomePageActivity::class.java))
                                requireActivity().finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Database error: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(requireContext(), "Auth failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // --- Google Sign-In setup ---
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // --- Activity Result Launcher for Google Sign-In ---
        val googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(Exception::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        // --- Google Sign-In button click ---
        googleSignInButton.setOnClickListener {
            // Force account chooser every time
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }
        }

        return view
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: ""
                val email = auth.currentUser?.email ?: ""
                val displayName = auth.currentUser?.displayName ?: ""
                val names = displayName.split(" ")
                val firstName = names.getOrNull(0) ?: ""
                val lastName = names.getOrNull(1) ?: ""
                val user = User(firstName, lastName, email, dob = "", city = "")

                // Save Google user to database if not exists
                database.getReference("users").child(uid).setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Google Sign-In Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), HomePageActivity::class.java))
                        requireActivity().finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Database error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Auth failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
