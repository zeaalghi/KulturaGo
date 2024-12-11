package com.dicoding.picodiploma.kulturago.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.kulturago.MainActivity
import com.dicoding.picodiploma.kulturago.R
import com.dicoding.picodiploma.kulturago.SplashActivity
import com.dicoding.picodiploma.kulturago.data.repository.FirebaseRepository
import com.dicoding.picodiploma.kulturago.ui.register.RegisterActivity
import com.dicoding.picodiploma.kulturago.util.Result

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy {
        LoginViewModel(FirebaseRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerNow = findViewById<TextView>(R.id.registerNow)
        val eyeIcon = findViewById<ImageView>(R.id.eyeIcon)

        var isPasswordVisible = false
        eyeIcon.setOnClickListener {
            if (isPasswordVisible) {
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye_off)
            } else {
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye_on)
            }
            isPasswordVisible = !isPasswordVisible
            passwordInput.setSelection(passwordInput.text.length)
        }

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, result.data, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Result.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerNow.setOnClickListener {
            // Navigate to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}

