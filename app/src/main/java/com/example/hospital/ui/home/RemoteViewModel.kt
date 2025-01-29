package com.example.hospital.ui.home

import androidx.lifecycle.ViewModel
import com.example.hospital.data.api.RetrofitInstance

class RemoteViewModel : ViewModel() {
    suspend fun getNurseImage(nurseId: Int): Result<ByteArray> {
        return try {
            val response = RetrofitInstance.api.getNurseImage(nurseId)
            if (response.isSuccessful) {
                response.body()?.bytes()?.takeIf { it.isNotEmpty() }?.let { bytes ->
                    Result.success(bytes)
                } ?: Result.failure(Exception("Empty image data"))
            } else {
                Result.failure(Exception("Failed to fetch image: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
