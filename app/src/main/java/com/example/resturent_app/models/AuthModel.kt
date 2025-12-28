package com.example.resturent_app.models

// Login ke liye data
data class LoginRequest(
    val email: String,
    val password: String
)

// Register ke liye data (Name, Email, Password)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

// Server se aane wala jawab (Login aur Register dono ke liye same hai)
data class AuthResponse(
    val success: Boolean,
    val message: String?,
    val data: UserData?
)

// User ki details
data class UserData(
    val id: Int,
    val name: String,
    val email: String
)