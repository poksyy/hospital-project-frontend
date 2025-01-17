package com.example.hospital.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class Nurse(val name: String = "", val age: Int)

sealed interface RemoteMessageUiState {
    data class Success(val remoteMessage: Response<List<Nurse>>) : RemoteMessageUiState
    object Error : RemoteMessageUiState
    object Loading : RemoteMessageUiState
}

interface RemoteNurseApi {
    @GET("/nurse/directory")
    suspend fun getRemoteNurse():  Response<List<Nurse>>

    @POST("nurse/authentication")
    suspend fun login(@Body nurse: Nurse): Response<String>

    @GET("nurse/directory")
    suspend fun getAllNurses(): Response<List<Nurse>>

    @GET("nurse/search-by-name/{name}")
    suspend fun findByName(@Path("name") name: String): Response<Nurse>

    @POST("nurse/registration")
    suspend fun createNurse(@Body nurse: Nurse): Response<Nurse>

    @GET("nurse/profile/{id}")
    suspend fun getNurseProfile(@Path("id") id: Int): Response<Nurse>

    @PUT("nurse/modification/{id}")
    suspend fun updateNurse(@Path("id") id: Int, @Body nurse: Nurse): Response<Nurse>

    @DELETE("nurse/deletion/{id}")
    suspend fun deleteNurse(@Path("id") id: Int): Response<String>
}

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080"

    val api: RemoteNurseApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RemoteNurseApi::class.java)
    }
}

class RemoteViewModel : ViewModel() {
    var remoteMessageUiState: RemoteMessageUiState by mutableStateOf(RemoteMessageUiState.Loading)
        private set

    fun getRemoteNurse() {
        viewModelScope.launch {
            remoteMessageUiState = RemoteMessageUiState.Loading
            try {
                val infoNurse = RetrofitInstance.api.getRemoteNurse()
                Log.d("RemoteViewModel", "Nurse fetched: ${infoNurse}")
                remoteMessageUiState = RemoteMessageUiState.Success(infoNurse)
            } catch (e: Exception) {
                Log.e("RemoteViewModel", "Error fetching nurse: ${e.message}")
                remoteMessageUiState = RemoteMessageUiState.Error
            }
        }
    }
}
