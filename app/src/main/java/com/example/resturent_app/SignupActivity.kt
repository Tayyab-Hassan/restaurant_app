package com.example.resturent_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // UI Variables
    private lateinit var btnSignup: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Init Views
        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPass = findViewById<EditText>(R.id.etConfirmPassword)
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val tvGoToLogin = findViewById<TextView>(R.id.tvGoToLogin)

        // Initialize Button and Progress Bar
        btnSignup = findViewById(R.id.btnSignup)
        progressBar = findViewById(R.id.progressBar)

        btnSignup.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()
            val confirmPass = etConfirmPass.text.toString().trim()

            // --- VALIDATION ---
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbTerms.isChecked) {
                Toast.makeText(this, "You must agree to the Terms & Conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // --- END VALIDATION ---

            // START LOADING
            showLoading(true)

            // Create User
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            saveUserToFirestore(userId, name, email)
                        } else {
                            // Should not happen, but safe to handle
                            showLoading(false)
                        }
                    } else {
                        // FAILURE: Stop loading
                        showLoading(false)
                        Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        tvGoToLogin.setOnClickListener {
            finish()
        }
    }

    private fun saveUserToFirestore(uid: String, name: String, email: String) {
        val userMap = hashMapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("users").document(uid).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
                // No need to stop loading here as we are leaving the screen
            }
            .addOnFailureListener { e ->
                // FAILURE: Stop loading
                showLoading(false)
                Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Helper function to toggle UI state
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            btnSignup.text = "" // Hide text
            btnSignup.isEnabled = false // Prevent double clicks
            progressBar.visibility = View.VISIBLE // Show spinner
        } else {
            btnSignup.text = "Sign Up" // Show text
            btnSignup.isEnabled = true // Enable click
            progressBar.visibility = View.GONE // Hide spinner
        }
    }
}