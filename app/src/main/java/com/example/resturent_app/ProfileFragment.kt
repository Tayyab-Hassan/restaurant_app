package com.example.resturent_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.resturent_app.utils.UserSession

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Initialize Views (Connect to XML)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnMyOrders = view.findViewById<Button>(R.id.btnMyOrders)

        btnMyOrders.setOnClickListener {
            val intent = Intent(activity, com.example.resturent_app.MyOrdersActivity::class.java)
            startActivity(intent)
        }

        // 2. Get Data from UserSession (Jo Login karte waqt save kiya tha)
        val session = UserSession(requireContext())

        if (session.isLoggedIn()) {
            // Data set karein
            tvName.text = session.getUserName() // Name show hoga
            tvEmail.text = session.getUserEmail() // Email show hoga
        } else {
            // Agar galti se login nahi hai
            tvName.text = "Guest User"
            tvEmail.text = "Please login"
        }

        // 3. Logout Logic
        btnLogout.setOnClickListener {
            // Session clear karein (Mobile se data delete)
            session.logout()

            // Login Screen par wapis jayein
            val intent = Intent(activity, LoginActivity::class.java)

            // Ye flags back button ko disable karte hain taaki user wapis profile par na aa sake
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}