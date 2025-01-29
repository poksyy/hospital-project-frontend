package com.example.hospital.ui.nurses.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.ui.home.RemoteViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                val result = remoteViewModel.getNurseDirectory()

                result.fold(
                    onSuccess = { nurses ->
                        _nurses.value = nurses
                        _error.value = null
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Unknown error occurred"
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}