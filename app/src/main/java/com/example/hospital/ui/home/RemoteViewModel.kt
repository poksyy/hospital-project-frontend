package com.example.hospital.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.RetrofitInstance
import com.example.hospital.data.api.RemoteNurseApi
import com.example.hospital.data.api.Nurse
import kotlinx.coroutines.launch
import retrofit2.Response

sealed interface RemoteMessageUiState {
    data class Success(val remoteMessage: Response<List<Nurse>>) : RemoteMessageUiState
    object Error : RemoteMessageUiState
    object Loading : RemoteMessageUiState
}

class RemoteViewModel : ViewModel() {
    var remoteMessageUiState: RemoteMessageUiState by mutableStateOf(RemoteMessageUiState.Loading)
        private set

    fun getRemoteNurse() {
        viewModelScope.launch {
            remoteMessageUiState = RemoteMessageUiState.Loading
            try {
                val response = RetrofitInstance.api.getRemoteNurse()
                Log.d("RemoteViewModel", "Nurses fetched: $response")
                if (response.isSuccessful) {
                    remoteMessageUiState = RemoteMessageUiState.Success(response)
                } else {
                    remoteMessageUiState = RemoteMessageUiState.Error
                }
            } catch (e: Exception) {
                Log.e("RemoteViewModel", "Error fetching nurse: ${e.message}")
                remoteMessageUiState = RemoteMessageUiState.Error
            }
        }
    }
}

