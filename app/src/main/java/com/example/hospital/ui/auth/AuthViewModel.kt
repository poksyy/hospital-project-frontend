package com.example.hospital.ui.auth

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.None)
    val loginResult: StateFlow<LoginResult> = _loginResult

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
                Log.e("AuthViewModel", "Login error: ${e.message}")
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

    private fun resetLoginState() {
        _loginResult.value = LoginResult.None
    }
}

sealed class LoginResult {
    data object None : LoginResult()
    data object Loading : LoginResult()
    data class Success(val nurse: Nurse) : LoginResult()
    data class Failure(val error: String) : LoginResult()
}