package com.dicoding.picodiploma.kulturago.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.picodiploma.kulturago.R
import com.dicoding.picodiploma.kulturago.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // No longer initializing or setting up the toolbar
        // Removed: val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        // Removed: setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment_activity_home)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Handle logout
                FirebaseAuth.getInstance().signOut() // Firebase logout
                clearLoginState() // Clear shared preferences
                navigateToLoginPage() // Redirect to login page
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clearLoginState() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply() // Clear all stored data in shared preferences
    }

    private fun navigateToLoginPage() {
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java) // Change to your LoginActivity class
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Close the current activity
    }
}
