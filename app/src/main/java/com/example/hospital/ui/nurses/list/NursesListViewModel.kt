package com.example.hospital.ui.nurses.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance
import com.example.hospital.ui.home.RemoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NursesListViewModel : ViewModel() {
    private val remoteViewModel = RemoteViewModel()

    private val _nurses = MutableStateFlow<List<Nurse>>(emptyList())
    val nurses: StateFlow<List<Nurse>> = _nurses

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchNurses()
    }

    private fun fetchNurses() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = withContext(Dispatchers.IO) { getNurseDirectory() }

                result.fold(
                    onSuccess = { nursesList ->
                        val updatedNurses = nursesList.map { nurse ->
                            nurse.id?.let {
                                val imageResult =
                                    withContext(Dispatchers.IO) { remoteViewModel.getNurseImage(it) }
                                imageResult.fold(
                                    onSuccess = { imageBytes -> nurse.copy(profileImage = imageBytes) },
                                    onFailure = { nurse }
                                )
                            } ?: nurse
                        }
                        _nurses.value = updatedNurses
                        _error.value = null
                    },
                    onFailure = { exception -> _error.value = exception.message ?: "Unknown error" }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getNurseDirectory(): Result<List<Nurse>> {
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
