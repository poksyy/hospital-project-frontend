package com.example.hospital.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _nurse = MutableStateFlow<Nurse?>(null)
    val nurse: StateFlow<Nurse?> = _nurse.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        startLoading()
        clearError()

        // Example with nurseID 1
        val nurseId = 1

        viewModelScope.launch {
            executeGetProfile(nurseId)
        }
    }

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
            _nurse.value = response.body()
            Log.d("ProfileViewModel", "Profile loaded successfully: ${response.body()}")
        } else {
            setError("Error loading profile: ${response.code()}")
            Log.e("ProfileViewModel", "Error loading profile: ${response.code()}")
        }
    }

    fun updateProfile(updatedNurse: Nurse) {
        val currentNurseId = _nurse.value?.id
        if (currentNurseId == null) {
            setError("No nurse ID available")
            return
        }

        startLoading()
        clearError()

        viewModelScope.launch {
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
            _nurse.value = response.body()
            Log.d("ProfileViewModel", "Profile updated successfully: ${response.body()}")
        } else {
            setError("Error updating profile: ${response.code()}")
            Log.e("ProfileViewModel", "Error updating profile: ${response.code()}")
        }
    }

    fun deleteProfile() {
        val currentNurseId = _nurse.value?.id
        if (currentNurseId == null) {
            setError("No nurse ID available")
            return
        }

        startLoading()
        clearError()

        viewModelScope.launch {
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
            Log.d("ProfileViewModel", "Profile deleted successfully")
        } else {
            setError("Error deleting profile: ${response.code()}")
            Log.e("ProfileViewModel", "Error deleting profile: ${response.code()}")
        }
    }

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