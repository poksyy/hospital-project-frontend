package com.example.hospital.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance

sealed interface RemoteMessageUiState {
    data class Success(val nurses: List<Nurse>) : RemoteMessageUiState
    data class Error(val message: String) : RemoteMessageUiState
    object Loading : RemoteMessageUiState
}

class RemoteViewModel : ViewModel() {
    var remoteMessageUiState: RemoteMessageUiState by mutableStateOf(RemoteMessageUiState.Loading)
        private set

    suspend fun postRegistration(user: String, password: String): Result<Nurse> {
        return try {
            val nurse = Nurse(user = user, password = password)
            val response = RetrofitInstance.api.postRegistration(nurse)
            Log.d("Register", "Registration successful: $response")

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("Register", "Registration error: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getNurseDirectory(): Result<List<Nurse>> {
        remoteMessageUiState = RemoteMessageUiState.Loading

        return try {
            val response = RetrofitInstance.api.getNurseDirectory()
            Log.d("NurseDirectory", "Nurses fetched: $response")

            if (response.isSuccessful) {
                response.body()?.let { nurses ->
                    remoteMessageUiState = RemoteMessageUiState.Success(nurses)
                    Result.success(nurses)
                } ?: run {
                    val error = "Empty response body"
                    remoteMessageUiState = RemoteMessageUiState.Error(error)
                    Result.failure(Exception(error))
                }
            } else {
                val error = "Failed to fetch nurses: ${response.code()}"
                remoteMessageUiState = RemoteMessageUiState.Error(error)
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Log.e("NurseDirectory", "Error fetching nurses: ${e.message}")
            remoteMessageUiState = RemoteMessageUiState.Error(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }
}