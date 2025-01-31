package com.example.hospital.image

import com.example.hospital.data.api.Nurse

// Sealed class to represent the UI state for nurse data (loading, success, error).
sealed class NursesUiState {

    data class Success(val nurses: List<Nurse>) : NursesUiState()
    data class Error(val message: String) : NursesUiState()
    data object Loading : NursesUiState()
}