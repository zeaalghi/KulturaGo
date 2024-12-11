package com.dicoding.picodiploma.kulturago.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.kulturago.data.repository.FirebaseRepository
import kotlinx.coroutines.launch
import com.dicoding.picodiploma.kulturago.util.Result

class LoginViewModel(private val repository: FirebaseRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.login(email, password)
                _loginResult.value = Result.Success(result)
            } catch (e: Exception) {
                _loginResult.value = Result.Error(e.message ?: "Unknown Error")
            }
        }
    }

    val jwtToken: String?
        get() = repository.getJwtToken()
}
