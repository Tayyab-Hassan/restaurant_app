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
import com.example.resturent_app.api.RetrofitClient
import com.example.resturent_app.models.AuthResponse
import com.example.resturent_app.models.RegisterRequest
import com.example.resturent_app.utils.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    // UI Variables (Jo XML main IDs hain)
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText // New Field
    private lateinit var cbTerms: CheckBox           // New Field
    private lateinit var btnSignup: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvGoToLogin: TextView       // Ye XML main TextView hai

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup) // Make sure XML file name matches

        // 1. Initialize Views (Connect XML IDs to Kotlin Variables)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        cbTerms = findViewById(R.id.cbTerms)
        btnSignup = findViewById(R.id.btnSignup)
        progressBar = findViewById(R.id.progressBar)
        tvGoToLogin = findViewById(R.id.tvGoToLogin)

        // 2. Signup Button Click
        btnSignup.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()
            val confirmPass = etConfirmPassword.text.toString().trim()

            // --- VALIDATION START ---

            // Empty Fields Check
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Password Match Check
            if (pass != confirmPass) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Terms Checkbox Check
            if (!cbTerms.isChecked) {
                Toast.makeText(this, "Please accept Terms & Conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // --- VALIDATION END ---

            // 3. Start Loading
            showLoading(true)

            // 4. API Call
            val request = RegisterRequest(name, email, pass)

            RetrofitClient.instance.register(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    showLoading(false) // Stop Loading

                    val body = response.body()

                    // Check if registration was successful (Status 201 / Success True)
                    if (response.isSuccessful && body != null && body.success) {
                        val userData = body.data

                        if (userData != null) {
                            // User Data Save (Auto Login)
                            val session = UserSession(this@SignupActivity)
                            session.saveUser(userData.id, userData.name, userData.email)

                            Toast.makeText(this@SignupActivity, "Registration Successful!", Toast.LENGTH_SHORT).show()

                            // Go to Home Screen
                            val intent = Intent(this@SignupActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // Error handling (e.g. User already exists)
                        val errorMsg = body?.message ?: "Registration Failed"
                        Toast.makeText(this@SignupActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@SignupActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 5. Go to Login Click
        tvGoToLogin.setOnClickListener {
            // Close Signup, go back to Login
            finish()
        }
    }

    // Helper function for UI loading state
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            btnSignup.text = "" // Text hide karo
            btnSignup.isEnabled = false // Click disable karo
            progressBar.visibility = View.VISIBLE // Spinner dikhao
        } else {
            btnSignup.text = "Sign Up" // Text wapis lao
            btnSignup.isEnabled = true // Click enable karo
            progressBar.visibility = View.GONE // Spinner chupao
        }
    }
}