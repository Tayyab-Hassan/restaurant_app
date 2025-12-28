package com.example.resturent_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.resturent_app.utils.CartManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Load Cart Data
        CartManager.loadCartFromPrefs(this)

        // Setup Bottom Nav
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Note: Sometimes findFragmentById returns null if layout isn't set,
        // so checking for null is safer, but usually this works fine if XML is correct.
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        if (navHostFragment != null) {
            val navController = navHostFragment.navController
            bottomNav.setupWithNavController(navController)
        }
    }
}