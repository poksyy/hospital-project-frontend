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

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess.asStateFlow()

    // Initialize the profile loading process.
    init {
        loadProfile()
    }

    private fun loadProfile() {
        val nurseId = getApplication<Application>()
            // Retrieve the nurse ID from SharedPreferences, which was saved during login.
            .getSharedPreferences("nurse_prefs", Context.MODE_PRIVATE)
            .getInt("logged_nurse_id", -1)

        if (nurseId == -1) {
            _error.value = "No logged nurse found"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitInstance.api.getNurseById(nurseId)
                if (response.isSuccessful) {
                    _nurse.value = response.body()
                } else {
                    _error.value = "Error loading profile: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("ProfileViewModel", "Error loading profile", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(updatedNurse: Nurse) {

        _updateSuccess.value = false
        // Check if the ID is null.
        val nurseId = _nurse.value?.id ?: return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitInstance.api.updateNurseProfile(nurseId, updatedNurse)
                if (response.isSuccessful) {
                    _nurse.value = response.body()
                    _updateSuccess.value = true
                } else {
                    _updateSuccess.value = false
                    _error.value = "Error updating profile: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("ProfileViewModel", "Error updating profile", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetUpdateSuccess() {
        _updateSuccess.value = false
    }

    // Delete the nurse profile and clear SharedPreferences.
    fun deleteProfile() {
        val nurseId = _nurse.value?.id ?: return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitInstance.api.deleteNurse(nurseId)
                if (response.isSuccessful) {
                    _nurse.value = null
                    getApplication<Application>()
                        .getSharedPreferences("nurse_prefs", Context.MODE_PRIVATE)
                        .edit()
                        .remove("logged_nurse_id")
                        .apply()
                } else {
                    _error.value = "Error deleting profile: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("ProfileViewModel", "Error deleting profile", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
