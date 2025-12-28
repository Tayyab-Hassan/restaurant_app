package com.example.resturent_app.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)


data class AuthResponse(
    val success: Boolean,
    val status: Int,
    val message: String,
    val data: UserData?
)


data class UserData(
    val id: Int,
    val name: String,
    val email: String
)