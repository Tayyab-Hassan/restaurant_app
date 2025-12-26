package com.example.resturent_app.models

// Server ko bhejne wala data
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)