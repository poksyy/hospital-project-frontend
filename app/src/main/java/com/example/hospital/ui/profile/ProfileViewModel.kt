package com.example.hospital.ui.profile

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // UI state flows
    private val _nurse = MutableStateFlow<Nurse?>(null)
    val nurse: StateFlow<Nurse?> = _nurse.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // First method to execute.
        loadProfile()
    }

    // Load nurse profile from API using stored ID
    private fun loadProfile() {
        startLoading()
        clearError()

        // Get nurse ID from SharedPreferences
        val nurseId = getApplication<Application>()
            .getSharedPreferences("nurse_prefs", Context.MODE_PRIVATE)
            .getInt("logged_nurse_id", -1)

        if (nurseId == -1) {
            setError("No logged nurse found")
            stopLoading()
            return
        }

        viewModelScope.launch {
            // Invoke method with logic to display the nurse profile.
            executeGetProfile(nurseId)
        }
    }

    // Execute API call to get profile
    private suspend fun executeGetProfile(nurseId: Int) {
        try {
            val response = RetrofitInstance.api.getProfile(nurseId)
            handleGetProfileResponse(response)
        } catch (e: Exception) {
            handleError("Error loading profile", e)
        } finally {
            stopLoading()
        }
    }

    private fun handleGetProfileResponse(response: retrofit2.Response<Nurse>) {
        if (response.isSuccessful) {
            // Update the state with the profile data.
            _nurse.value = response.body()
        } else {
            setError("Error loading profile: ${response.code()}")
        }
    }

    // Update nurse profile
    fun updateProfile(updatedNurse: Nurse) {
        val currentNurseId = _nurse.value?.id ?: run {
            setError("No nurse ID available")
            return
        }

        startLoading()
        clearError()

        viewModelScope.launch {
            // Invoke method with logic to update the nurse profile.
            executeUpdateProfile(currentNurseId, updatedNurse)
        }
    }

    private suspend fun executeUpdateProfile(nurseId: Int, updatedNurse: Nurse) {
        try {
            val response = RetrofitInstance.api.updateNurse(nurseId, updatedNurse)
            handleUpdateProfileResponse(response)
        } catch (e: Exception) {
            handleError("Error updating profile", e)
        } finally {
            stopLoading()
        }
    }

    private fun handleUpdateProfileResponse(response: retrofit2.Response<Nurse>) {
        if (response.isSuccessful) {
            // Update the state with the updated data.
            _nurse.value = response.body()
        } else {
            setError("Error updating profile: ${response.code()}")
        }
    }

    // Delete nurse profile
    fun deleteProfile() {
        val currentNurseId = _nurse.value?.id ?: run {
            setError("No nurse ID available")
            return
        }

        startLoading()
        clearError()

        viewModelScope.launch {
            // Invoke method with logic to delete the nurse profile.
            executeDeleteProfile(currentNurseId)
        }
    }

    private suspend fun executeDeleteProfile(nurseId: Int) {
        try {
            val response = RetrofitInstance.api.deleteNurse(nurseId)
            handleDeleteProfileResponse(response)
        } catch (e: Exception) {
            handleError("Error deleting profile", e)
        } finally {
            stopLoading()
        }
    }

    private fun handleDeleteProfileResponse(response: retrofit2.Response<String>) {
        if (response.isSuccessful) {
            _nurse.value = null
            // Clear user data after successful deletion.
            getApplication<Application>()
                .getSharedPreferences("nurse_prefs", Context.MODE_PRIVATE)
                .edit()
                .remove("logged_nurse_id")
                .apply()
        } else {
            setError("Error deleting profile: ${response.code()}")
        }
    }

    // Utility functions for state management
    private fun startLoading() {
        _isLoading.value = true
    }

    private fun stopLoading() {
        _isLoading.value = false
    }

    private fun clearError() {
        _error.value = null
    }

    private fun setError(message: String) {
        _error.value = message
    }

    private fun handleError(message: String, e: Exception) {
        setError("$message: ${e.message}")
        Log.e("ProfileViewModel", message, e)
    }
}
