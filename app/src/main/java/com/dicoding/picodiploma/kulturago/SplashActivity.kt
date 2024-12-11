package com.dicoding.picodiploma.kulturago

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.kulturago.ui.home.HomeActivity
import com.dicoding.picodiploma.kulturago.ui.login.LoginActivity
import com.dicoding.picodiploma.kulturago.data.repository.FirebaseRepository
import com.dicoding.picodiploma.kulturago.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseRepository: FirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseRepository = FirebaseRepository(this)


        Handler().postDelayed({
            navigateToNextScreen()
        }, 2000)
    }

    private fun isUserLoggedIn(): Boolean {
        val jwtToken = firebaseRepository.getJwtToken()
        val firebaseUser = firebaseAuth.currentUser
        return jwtToken != null && firebaseUser != null
    }

    private fun navigateToNextScreen() {
        if (isUserLoggedIn()) {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}
