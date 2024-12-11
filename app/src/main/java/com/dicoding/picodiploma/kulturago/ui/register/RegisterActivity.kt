package com.dicoding.picodiploma.kulturago.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.kulturago.data.repository.FirebaseRepository
import com.dicoding.picodiploma.kulturago.databinding.ActivityRegisterBinding
import com.dicoding.picodiploma.kulturago.ui.login.LoginActivity
import com.dicoding.picodiploma.kulturago.util.Result

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(FirebaseRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val textLogin = binding.loginNow
        binding.registerButton.setOnClickListener {
            val fullName = binding.fullNameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()


            if (password == confirmPassword) {
                viewModel.register(fullName, email, password)
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
        textLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, result.data, Toast.LENGTH_SHORT).show()
                    finish()
                }

                is Result.Error -> Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
