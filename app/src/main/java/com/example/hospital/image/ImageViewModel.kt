package com.example.hospital.image

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.RetrofitInstance
import kotlinx.coroutines.launch

// ViewModel to manage nurse data and handle API calls.
class ImageViewModel : ViewModel() {

    // Holds the current state of nurse data (loading, success, or error).
    var uiState: NursesUiState by mutableStateOf(NursesUiState.Loading)

    // Calls fetchNurses() method when ViewModel is initialized.
    init {
        fetchNurses()
    }

    // Fetches nurse data from the API.
    private fun fetchNurses() {
        viewModelScope.launch {
            uiState = NursesUiState.Loading
            try {
                val response = RetrofitInstance.api.getAllNurses()
                if (response.isSuccessful && response.body() != null) {
                    // Updates state with list of nurses if successful.
                    uiState = NursesUiState.Success(response.body()!!)
                } else {
                    // Handles error if API response is not successful.
                    uiState = NursesUiState.Error("Error loading nurses: ${response.message()}")
                }
            } catch (e: Exception) {
                // Handles any exceptions during the API call.
                uiState = NursesUiState.Error("Error: ${e.message}")
            }
        }
    }

    // Fetches an image for a specific nurse by ID.
    suspend fun getNurseImage(nurseId: Int): Result<ByteArray> {
        return try {
            val response = RetrofitInstance.api.getNurseImage(nurseId)
            if (response.isSuccessful) {
                // Returns the nurse image data if successful.
                response.body()?.bytes()?.takeIf { it.isNotEmpty() }?.let { bytes ->
                    Result.success(bytes)
                } ?: Result.failure(Exception("Empty image data"))
            } else {
                // If nurse data hasn't been loaded successfully, return a failure.
                Result.failure(Exception("Failed to fetch image: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Catches and handles any exception while fetching the image.
            Result.failure(e)
        }
    }
}
