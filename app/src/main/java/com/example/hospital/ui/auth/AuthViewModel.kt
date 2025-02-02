package com.example.hospital.ui.auth

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // StateFlow for login and register results, holding different states (success, failure, loading).
    private val _loginResult =
        MutableStateFlow<AuthUiState.LoginResult>(AuthUiState.LoginResult.None)

    val loginResult: StateFlow<AuthUiState.LoginResult> = _loginResult

    val _registerResult =
        MutableStateFlow<AuthUiState.RegisterResult>(AuthUiState.RegisterResult.None)

    val registerResult: StateFlow<AuthUiState.RegisterResult> = _registerResult

    // Holds the current values for username and password.
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")

    // Login function that checks for empty fields, makes a network request, and updates the state.
    fun login() {
        if (username.isBlank() || password.isBlank()) {
            _loginResult.value =
                AuthUiState.LoginResult.Failure("Username and password cannot be empty")
            return
        }

        val nurse = Nurse(user = username, password = password)

        viewModelScope.launch {
            _loginResult.value = AuthUiState.LoginResult.Loading
            try {
                val response = RetrofitInstance.api.login(nurse)
                if (response.isSuccessful && response.body() != null) {
                    val loggedNurse = response.body()!!
                    // If login is successful, store the nurse's ID in SharedPreferences for future use.
                    loggedNurse.id?.let { nurseId ->
                        getApplication<Application>().getSharedPreferences(
                            "nurse_prefs",
                            Context.MODE_PRIVATE
                        )
                            .edit()
                            .putInt("logged_nurse_id", nurseId)
                            .apply()
                    }
                    _loginResult.value = AuthUiState.LoginResult.Success(loggedNurse)
                } else {
                    _loginResult.value =
                        AuthUiState.LoginResult.Failure("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _loginResult.value = AuthUiState.LoginResult.Failure("Error: ${e.message}")
            }
        }
    }

    // Register function that validates input, checks if username is taken, and attempts registration.
    fun register(user: String, password: String, name: String) {

        if (user.isBlank() || password.isBlank() || name.isBlank()) {
            _registerResult.value = AuthUiState.RegisterResult.Failure("Username, password and name cannot be empty")
            return
        }

        val validationError = validateRegistrationInput(user, password)
        if (validationError != null) {
            _registerResult.value = AuthUiState.RegisterResult.Failure(validationError)
            return
        }

        viewModelScope.launch {
            _registerResult.value = AuthUiState.RegisterResult.Loading
            try {
                val userCheckResponse = RetrofitInstance.api.checkUserAvailability(user)

                if (!userCheckResponse.isSuccessful || userCheckResponse.body() == null) {
                    _registerResult.value = AuthUiState.RegisterResult.Failure(
                        "Error checking username availability: ${userCheckResponse.message()}"
                    )
                    return@launch
                }

                userCheckResponse.body()?.let { isAvailable ->
                    if (!isAvailable) {
                        _registerResult.value = AuthUiState.RegisterResult.Failure("Username is already taken")
                        return@launch
                    }
                } ?: run {
                    _registerResult.value = AuthUiState.RegisterResult.Failure("Invalid response from server")
                    return@launch
                }

                val nurse = Nurse(user = user, password = password, name = name)

                val response = RetrofitInstance.api.register(nurse)

                if (response.isSuccessful && response.body() != null) {
                    _registerResult.value = AuthUiState.RegisterResult.Success(response.body()!!)
                    username = ""
                    this@AuthViewModel.password = ""
                    this@AuthViewModel.name = ""

                    kotlinx.coroutines.delay(2000)
                    resetRegisterState()
                } else {
                    _registerResult.value = AuthUiState.RegisterResult.Failure("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _registerResult.value = AuthUiState.RegisterResult.Failure("Error: ${e.message}")
            }
        }
    }

    // Validates the registration input, including username and password requirements.
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

    // Resets the login state to its initial value.
    private fun resetLoginState() {
        _loginResult.value = AuthUiState.LoginResult.None
        username = ""
        password = ""
        name= ""
    }

    // Resets the registration state to its initial value.
    fun resetRegisterState() {
        _registerResult.value = AuthUiState.RegisterResult.None
        username = ""
        password = ""
        name= ""
    }

    // Logs out the user by removing their ID from SharedPreferences and resetting login state.
    fun logout() {
        getApplication<Application>().getSharedPreferences("nurse_prefs", Context.MODE_PRIVATE)
            .edit()
            .remove("logged_nurse_id")
            .apply()
        resetLoginState()
    }
}
