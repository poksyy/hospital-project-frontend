package com.example.hospital.ui.auth

import com.example.hospital.data.api.Nurse

// Class containing UI states related to authentication.
class AuthUiState {

    // Defines the possible results of the login operation.
    sealed class LoginResult {

        data object None : LoginResult()
        // Login operation in progress.
        data object Loading : LoginResult()
        // Login successful : NurseObject.
        data class Success(val nurse: Nurse) : LoginResult()
        // Login failed : errorMessage.
        data class Failure(val error: String) : LoginResult()
    }

    // Defines the possible results of the registration operation.
    sealed class RegisterResult {

        data object None : RegisterResult()
        // Register operation in progress.
        data object Loading : RegisterResult()
        // Register successful : NurseObject.
        data class Success(val nurse: Nurse) : RegisterResult()
        // Register failed : errorMessage.
        data class Failure(val error: String) : RegisterResult()
    }
}