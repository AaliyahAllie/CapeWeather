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

class LoginFragmentActivity : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val emailInput = view.findViewById<EditText>(R.id.emailInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val rememberMe = view.findViewById<CheckBox>(R.id.rememberMe)
        val forgotPassword = view.findViewById<TextView>(R.id.forgotPassword)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val googleSignInButton = view.findViewById<LinearLayout>(R.id.googleSignInLogin)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // --- Email/password login ---
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), HomePageActivity::class.java))
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // --- Forgot password ---
        forgotPassword.setOnClickListener {
            val email = emailInput.text.toString()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Reset link sent to $email", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Enter your email first", Toast.LENGTH_SHORT).show()
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
