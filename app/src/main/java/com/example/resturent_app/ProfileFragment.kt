package com.example.resturent_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvUid = view.findViewById<TextView>(R.id.tvUid)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        // 1. Get Firebase User
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            tvEmail.text = user.email
            tvUid.text = "UID: ${user.uid}"
        } else {
            tvEmail.text = "Guest"
            tvUid.text = ""
        }

        // 2. Logout Logic
        btnLogout.setOnClickListener {
            // Sign out from Firebase
            FirebaseAuth.getInstance().signOut()

            // Navigate back to LoginActivity
            // IMPORTANT: Replace 'LoginActivity' with whatever your actual Login screen class name is.
            // If you don't have one yet, change this to just finish() for now.
            val intent = Intent(activity, LoginActivity::class.java)

            // Clear the back stack so the user cannot press "Back" to return to the profile
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}