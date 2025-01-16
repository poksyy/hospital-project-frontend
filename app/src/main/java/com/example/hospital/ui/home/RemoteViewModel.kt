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
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class Nurse(val name: String="", val age:Number)

sealed interface RemoteMessageUiState {
    data class Success(
        val remoteMessage: Nurse
    ) : RemoteMessageUiState
    object Error : RemoteMessageUiState
    object Loading : RemoteMessageUiState
}

interface RemoteNurseApi {
    @GET("nurse")
    suspend fun getRemoteNurse():Nurse

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

class RemoteViewModel: ViewModel(){
    var remoteMessageUiState: RemoteMessageUiState by mutableStateOf(RemoteMessageUiState.Loading)
    private set
    fun getRemoteNurse(){
        viewModelScope.launch {
            remoteMessageUiState=RemoteMessageUiState.Loading
            try {
                val connection= Retrofit.Builder().baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                val endPoint = connection.create(RemoteNurseApi::class.java)
                val infoNurse = endPoint.getRemoteNurse()
                Log.d("exemple", "RESULT ${infoNurse.name}")
                remoteMessageUiState=RemoteMessageUiState.Success(infoNurse)
            } catch (e:Exception){
                Log.d("exemple", "ERROR GET ${e.message} ${e.printStackTrace()}")
                remoteMessageUiState=RemoteMessageUiState.Error
            }

        }
    }
}