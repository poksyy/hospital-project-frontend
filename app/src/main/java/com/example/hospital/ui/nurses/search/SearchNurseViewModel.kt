package com.example.hospital.ui.nurses.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchNurseViewModel : ViewModel() {
    // Holds the search text entered by the user.
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    // Holds the filtered nurses fetched from the backend.
    private val _filteredNurses = MutableStateFlow<List<Nurse>>(emptyList())
    val filteredNurses: StateFlow<List<Nurse>> = _filteredNurses

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Function to search nurses by name based on the text entered
    fun searchNurses() {
        val name = _searchText.value
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (name.isNotBlank()) {
                    // Perform search by name
                    val nursesResult = searchNursesByName(name)
                    nursesResult.fold(
                        onSuccess = { nurses ->
                            if (nurses != null) {
                                _filteredNurses.value = listOf(nurses)
                                _error.value = null // Clear any previous error
                            } else {
                                // No nurse found with the given name
                                _error.value = "No nurse found with the name: $name"
                                _filteredNurses.value = emptyList()
                            }
                        },
                        onFailure = { exception ->
                            _error.value = exception.message ?: "Unknown error"
                            _filteredNurses.value = emptyList()
                        }
                    )
                } else {
                    // If the search text is empty, get all nurses
                    val allNursesResult = getAllNurses()
                    allNursesResult.fold(
                        onSuccess = { nurses ->
                            _filteredNurses.value = nurses
                            _error.value = null // Clear any previous error
                        },
                        onFailure = { exception ->
                            _error.value = exception.message ?: "Unknown error"
                            _filteredNurses.value = emptyList()
                        }
                    )
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _filteredNurses.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    // Call the API to search for nurses by name
    private suspend fun searchNursesByName(name: String): Result<Nurse?> {
        return try {
            val response = RetrofitInstance.api.getNurseByName(name)
            if (response.isSuccessful) {
                response.body()?.let { nurse ->
                    Result.success(nurse)
                } ?: Result.success(null)  // Return null if no nurse found
            } else {
                Result.failure(Exception("Nurse not found."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Call the API to get all nurses
    private suspend fun getAllNurses(): Result<List<Nurse>> {
        return try {
            val response = RetrofitInstance.api.getAllNurses()
            if (response.isSuccessful) {
                response.body()?.let { nurses ->
                    Result.success(nurses)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("API call failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update the search text and perform a filtered search
    fun updateSearchText(newText: String) {
        _searchText.value = newText
        searchNurses() // Call the search whenever the text changes
    }
}