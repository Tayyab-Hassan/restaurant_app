package com.example.resturent_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    // UI Variables
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnGoToSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Auto Login Check
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Initialize Views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar) // Init Progress Bar
        btnGoToSignup = findViewById(R.id.btnGoToSignup)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            // 1. Validation
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Start Loading
            showLoading(true)

            // 3. Firebase Auth
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        // Note: No need to stop loading here as activity closes
                    } else {
                        // Failure: Stop loading and show error
                        showLoading(false)
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnGoToSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    // Helper to toggle button state
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            btnLogin.text = "" // Hide "Log In" text
            btnLogin.isEnabled = false // Disable click
            progressBar.visibility = View.VISIBLE // Show Spinner
        } else {
            btnLogin.text = "Log In" // Restore text
            btnLogin.isEnabled = true // Enable click
            progressBar.visibility = View.GONE // Hide Spinner
        }
    }
}