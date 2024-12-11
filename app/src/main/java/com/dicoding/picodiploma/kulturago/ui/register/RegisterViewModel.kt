package com.dicoding.picodiploma.kulturago.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.kulturago.data.repository.FirebaseRepository
import com.dicoding.picodiploma.kulturago.util.Result
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: FirebaseRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult

    fun register(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.register(fullName, email, password)
                _registerResult.value = Result.Success(result)
            } catch (e: Exception) {
                _registerResult.value = Result.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
