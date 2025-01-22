package com.example.hospital.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response

// Sealed interface representing the UI state for remote messages.
sealed interface RemoteMessageUiState {
    data class Success(val remoteMessage: Response<List<Nurse>>) : RemoteMessageUiState
    object Error : RemoteMessageUiState
    object Loading : RemoteMessageUiState
}

// ViewModel to manage UI data.
class RemoteViewModel : ViewModel() {
    var remoteMessageUiState: RemoteMessageUiState by mutableStateOf(RemoteMessageUiState.Loading)

    // Fetch remote nurse data.
    fun getRemoteNurse() {
        viewModelScope.launch {
            // Set loading state.
            remoteMessageUiState = RemoteMessageUiState.Loading
            try {
                // Fetch data from API.
                val response = RetrofitInstance.api.getRemoteNurse()
                Log.d("RemoteViewModel", "Nurses fetched: $response")
                remoteMessageUiState = if (response.isSuccessful) {
                    // Set success state.
                    RemoteMessageUiState.Success(response)
                } else {
                    // Handle API error.
                    handleError(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                // Handle exception.
                Log.e("RemoteViewModel", "Error fetching nurse: ${e.message}")
                remoteMessageUiState = handleError(e)
            }
        }
    }

    // Post nurse registration.
    suspend fun postRegistration(user: String, password: String): Result<Nurse> {
        return try {
            // Create nurse object.
            val nurse = Nurse(user = user, password = password)
            // Send registration request.
            val response = RetrofitInstance.api.postRegistration(nurse)
            Log.d("Register", "Registration successful: $response")
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("Register", "Registration error: ${e.message}")
            // Return failure result.
            Result.failure(e)
        }
    }

    // Function to handle errors and log them.
    private fun handleError(e: Exception): RemoteMessageUiState {
        Log.e("RemoteViewModel", "Error: ${e.message}")
        // Return error state.
        return RemoteMessageUiState.Error
    }
}
