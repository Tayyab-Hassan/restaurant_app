package com.example.resturent_app.utils

import android.content.Context
import android.content.SharedPreferences

class UserSession(context: Context) {
    // Mobile ki memory (SharedPreferences) ka naam
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // User ka data save karne ke liye
    fun saveUser(id: Int, name: String, email: String) {
        val editor = prefs.edit()
        editor.putInt("USER_ID", id)
        editor.putString("USER_NAME", name)
        editor.putString("USER_EMAIL", email)
        editor.putBoolean("IS_LOGGED_IN", true)
        editor.apply()
    }

    // Check karne ke liye ke user pehle se login hai ya nahi
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("IS_LOGGED_IN", false)
    }

    // User ki ID wapis lene ke liye
    fun getUserId(): String {
        return prefs.getInt("USER_ID", -1).toString()
    }

    // User ka Email lene ke liye
    fun getUserEmail(): String? {
        return prefs.getString("USER_EMAIL", "")
    }

    // --- YE MISSING THA (Ab Add kar diya hai) ---
    fun getUserName(): String? {
        return prefs.getString("USER_NAME", "User")
    }

    // Logout karne ke liye
    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}