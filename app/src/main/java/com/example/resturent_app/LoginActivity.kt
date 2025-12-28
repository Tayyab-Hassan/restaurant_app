package com.example.resturent_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.resturent_app.api.RetrofitClient
import com.example.resturent_app.models.AuthResponse
import com.example.resturent_app.models.LoginRequest
import com.example.resturent_app.utils.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    // UI Variables
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnGoToSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Session Check (Auto Login)
        // Firebase ki jagah ab hum apna session check karenge
        val session = UserSession(this)
        if (session.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Initialize Views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        btnGoToSignup = findViewById(R.id.btnGoToSignup)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            // Validation
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Start Loading
            showLoading(true)

            // --- API CALL START ---
            val request = LoginRequest(email, pass)

            RetrofitClient.instance.login(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    showLoading(false) // Stop Loading

                    val body = response.body()

                    // Server check: Response successful hai aur body.success true hai
                    if (response.isSuccessful && body != null && body.success) {
                        val userData = body.data

                        if (userData != null) {
                            // User Data ko mobile mein save karo
                            session.saveUser(userData.id, userData.name, userData.email)

                            Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()

                            // Go to Home
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Error: No user data found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Agar password galat hai ya user nahi mila
                        val errorMsg = body?.message ?: "Login Failed"
                        Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    showLoading(false) // Stop Loading
                    Toast.makeText(this@LoginActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
            // --- API CALL END ---
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