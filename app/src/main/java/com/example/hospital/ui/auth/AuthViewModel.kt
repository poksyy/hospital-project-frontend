package com.example.hospital.ui.auth

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.None)
    val loginResult: StateFlow<LoginResult> = _loginResult

    private val _registerResult = MutableStateFlow<RegisterResult>(RegisterResult.None)
    val registerResult: StateFlow<RegisterResult> = _registerResult

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun login() {
        if (username.isBlank() || password.isBlank()) {
            _loginResult.value = LoginResult.Failure("Username and password cannot be empty")
            return
        }

        val nurse = Nurse(user = username, password = password)

        viewModelScope.launch {
            _loginResult.value = LoginResult.Loading
            try {
                val response = RetrofitInstance.api.login(nurse)
                if (response.isSuccessful && response.body() != null) {
                    val loggedNurse = response.body()!!
                    // Save the nurse ID in the SharedPreferences
                    loggedNurse.id?.let { nurseId ->
                        getApplication<Application>().getSharedPreferences("nurse_prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putInt("logged_nurse_id", nurseId)
                            .apply()
                    }
                    _loginResult.value = LoginResult.Success(loggedNurse)
                } else {
                    _loginResult.value = LoginResult.Failure("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Failure("Error: ${e.message}")
            }
        }
    }

    fun logout() {
        // Clean SharedPreferences when the user logout.
        getApplication<Application>().getSharedPreferences("nurse_prefs", Context.MODE_PRIVATE)
            .edit()
            .remove("logged_nurse_id")
            .apply()

        resetLoginState()
    }

    fun register(user: String, password: String, onSuccess: () -> Unit = {}) {
        if (user.isBlank() || password.isBlank()) {
            _registerResult.value = RegisterResult.Failure("Username and password cannot be empty")
            return
        }

        val validationError = validateRegistrationInput(user, password)
        if (validationError != null) {
            _registerResult.value = RegisterResult.Failure(validationError)
            return
        }

        viewModelScope.launch {
            _registerResult.value = RegisterResult.Loading
            try {
                val userCheckResponse = RetrofitInstance.api.checkUserAvailability(user)
                if (!userCheckResponse.isSuccessful || userCheckResponse.body() == false) {
                    _registerResult.value = RegisterResult.Failure("Username is already taken")
                    return@launch
                }

                val nurse = Nurse(user = user, password = password)
                val response = RetrofitInstance.api.postRegistration(nurse)

                if (response.isSuccessful && response.body() != null) {
                    _registerResult.value = RegisterResult.Success(response.body()!!)
                    onSuccess()
                } else {
                    _registerResult.value =
                            RegisterResult.Failure("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _registerResult.value = RegisterResult.Failure("Error: ${e.message}")
            }
        }
    }

    private fun validateRegistrationInput(user: String, password: String): String? {
        if (user.isBlank()) {
            return "Username cannot be empty"
        }
        if (password.length < 8) {
            return "Password must be at least 8 characters long"
        }
        if (!password.any { it.isUpperCase() }) {
            return "Password must contain at least one uppercase letter"
        }
        if (!password.any { it.isDigit() }) {
            return "Password must contain at least one number"
        }
        return null
    }

    fun resetLoginState() {
        _loginResult.value = LoginResult.None
    }

    fun resetRegisterState() {
        _registerResult.value = RegisterResult.None
    }
}

sealed class LoginResult {
    data object None : LoginResult()
    data object Loading : LoginResult()
    data class Success(val nurse: Nurse) : LoginResult()
    data class Failure(val error: String) : LoginResult()
}

sealed class RegisterResult {
    object None : RegisterResult()
    object Loading : RegisterResult()
    data class Success(val nurse: Nurse) : RegisterResult()
    data class Failure(val error: String) : RegisterResult()
}