package com.example.hospital.ui.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class LoginResult {
    object Success : LoginResult()
    object Failure : LoginResult()
    object None : LoginResult()
}

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.None)
    val loginResult: StateFlow<LoginResult> = _loginResult

    private var _username: String = ""
    var username: String
        get() = _username
        set(value) {
            _username = value
        }

    private var _password: String = ""
    var password: String
        get() = _password
        set(value) {
            _password = value
        }

    fun login() {
        val correctUsername = "admin"
        val correctPassword = "123"

        if (username == correctUsername && password == correctPassword) {
            _loginResult.value = LoginResult.Success
        } else {
            _loginResult.value = LoginResult.Failure
        }
    }

    fun register() {
        // TODO Implement registration
    }
}
