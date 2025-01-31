package com.example.hospital.ui.nurses.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance
import com.example.hospital.image.ImageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NursesListViewModel : ViewModel() {

    // Create an instance of imageViewModel to invoke getNurseImage() method.
    private val imageViewModel = ImageViewModel()

    // StateFlow to manage the state of nurses: list of nurses, loading status, and error message.
    private val _nurses = MutableStateFlow<List<Nurse>>(emptyList())
    val nurses: StateFlow<List<Nurse>> = _nurses

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Calls fetchNurses() when ViewModel is initialized.
    init {
        fetchNurses()
    }

    // Fetches the list of nurses correctly and updates the state
    private fun fetchNurses() {
        viewModelScope.launch {
            try {
                // Indicate the Loading on true while fetching.
                _isLoading.value = true
                // Calls the API on a background thread to fetch the list of Nurses.
                val result = withContext(Dispatchers.IO) { getAllNurses() }
                result.fold(
                    // If the fetch is successful, it receives a list of Nurses from the API response.
                    onSuccess = { nursesList ->
                        // Iterates over the nurse list, checking if the ID is not null to fetch.
                        val updatedNurses = nursesList.map { nurse ->
                            // If ID is not null, we start the background thread to fetch image.
                            nurse.id?.let {
                                // Runs the image fetching process in a background thread.
                                val imageResult = withContext(Dispatchers.IO) {
                                    imageViewModel.getNurseImage(it)
                                }
                                // If successful, attach the fetched image to the nurse, otherwise keep the original data.
                                imageResult.fold(
                                    onSuccess = { imageBytes -> nurse.copy(profileImage = imageBytes) },
                                    onFailure = { nurse }
                                )
                                // If the ID is null, return the nurse object without attempting to fetch the image.
                            } ?: nurse
                        }
                        // Updates the nurse list with profile images.
                        _nurses.value = updatedNurses
                        // Clears any previous error if the operation was successful
                        _error.value = null
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Unknown error"
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fetches the nurse list from the API and returns the result, identifying each nurse by ID.
    private suspend fun getAllNurses(): Result<List<Nurse>> {
        return try {
            val response = RetrofitInstance.api.getAllNurses()
            
            if (response.isSuccessful) {
                response.body()?.let { nurses ->
                    Result.success(nurses)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to fetch nurses: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}