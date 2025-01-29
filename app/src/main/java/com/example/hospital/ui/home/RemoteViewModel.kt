package com.example.hospital.ui.home

import androidx.lifecycle.ViewModel
import com.example.hospital.data.api.RetrofitInstance

sealed interface RemoteMessageUiState {
    data class Success(val nurses: List<Nurse>) : RemoteMessageUiState
    data class Error(val message: String) : RemoteMessageUiState
    data object Loading : RemoteMessageUiState
}

class RemoteViewModel : ViewModel() {
    private var remoteMessageUiState: RemoteMessageUiState by mutableStateOf(RemoteMessageUiState.Loading)

    suspend fun getNurseImage(nurseId: Int): Result<ByteArray> {
        return try {
            val response = RetrofitInstance.api.getNurseImage(nurseId)
            if (response.isSuccessful) {
                response.body()?.bytes()?.takeIf { it.isNotEmpty() }?.let { bytes ->
                    Result.success(bytes)
                } ?: Result.failure(Exception("Empty image data"))
            } else {
                Result.failure(Exception("Failed to fetch image: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
