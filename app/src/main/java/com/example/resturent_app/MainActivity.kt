package com.example.resturent_app

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.resturent_app.utils.CartManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup Bottom Nav
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)

        // --- NEW: Fetch Cart Data from Firestore ---
        // We can show a small loading indicator if we want, or just load in background
        CartManager.fetchCartFromFirestore {
            // Data Loaded!
            // If you are on the Cart screen, it would need a refresh,
            // but since this is onCreate, the fragments haven't loaded data yet anyway.
        }
    }
}