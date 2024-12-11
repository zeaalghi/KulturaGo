package com.dicoding.picodiploma.kulturago

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.kulturago.ui.home.HomeActivity
import com.dicoding.picodiploma.kulturago.ui.login.LoginActivity
import com.dicoding.picodiploma.kulturago.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseRepository: FirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        firebaseAuth = FirebaseAuth.getInstance()
        firebaseRepository = FirebaseRepository(this)


        if (isUserLoggedIn()) {
            navigateToHome()
        } else {
            navigateToLogin()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val jwtToken = firebaseRepository.getJwtToken()
        val firebaseUser = firebaseAuth.currentUser
        return jwtToken != null && firebaseUser != null
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
