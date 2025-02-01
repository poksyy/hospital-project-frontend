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
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    // UI state flows
    private val _nurse = MutableStateFlow<Nurse?>(null)
    val nurse: StateFlow<Nurse?> = _nurse.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _updateMessage = MutableStateFlow<UpdateMessage?>(null)
    val updateMessage: StateFlow<UpdateMessage?> = _updateMessage.asStateFlow()

    data class UpdateMessage(
        val message: String,
        val isError: Boolean
    )

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val nurseId = getApplication<Application>()
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
        clearUpdateMessage()
        val nurseId = _nurse.value?.id ?: return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitInstance.api.updateNurseProfile(nurseId, updatedNurse)
                if (response.isSuccessful) {
                    _nurse.value = response.body()
                    setUpdateMessage("Profile updated successfully", false)
                } else {
                    throw HttpException(response)
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    400 -> setUpdateMessage("Username is already taken or fields are empty", true)
                    else -> setUpdateMessage("Error updating profile: ${e.code()}", true)
                }
                Log.e("ProfileViewModel", "HTTP error: ${e.code()}", e)
            } catch (e: IOException) {
                setUpdateMessage("Network error: Check your connection", true)
                Log.e("ProfileViewModel", "IO error", e)
            } catch (e: Exception) {
                setUpdateMessage("Unexpected error: ${e.message}", true)
                Log.e("ProfileViewModel", "Unexpected error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePassword(newPassword: String) {
        clearUpdateMessage()
        val nurseId = _nurse.value?.id ?: return
        val currentNurse = _nurse.value?.copy(password = newPassword) ?: return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitInstance.api.updateNursePassword(nurseId, currentNurse)
                if (response.isSuccessful) {
                    setUpdateMessage("Password updated successfully", false)
                } else {
                    throw HttpException(response)
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    400 -> setUpdateMessage("Invalid password format", true)
                    else -> setUpdateMessage("Error updating password: ${e.code()}", true)
                }
                Log.e("ProfileViewModel", "HTTP error updating password: ${e.code()}", e)
            } catch (e: IOException) {
                setUpdateMessage("Network error: Check your connection", true)
                Log.e("ProfileViewModel", "IO error updating password", e)
            } catch (e: Exception) {
                setUpdateMessage("Unexpected error: ${e.message}", true)
                Log.e("ProfileViewModel", "Unexpected error updating password", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setUpdateMessage(message: String, isError: Boolean) {
        _updateMessage.value = UpdateMessage(message, isError)
        // Automatically clear the message after 5 seconds.
        viewModelScope.launch {
            kotlinx.coroutines.delay(5000)
            clearUpdateMessage()
        }
    }

    private fun clearUpdateMessage() {
        _updateMessage.value = null
    }

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