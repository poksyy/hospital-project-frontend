package com.example.hospital.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RemoteNurseApi {

    companion object {
        private const val BASE_PATH = "hospital/"
    }

    @POST("${BASE_PATH}login")
    suspend fun login(@Body nurse: Nurse): Response<Nurse>

    @POST("${BASE_PATH}register")
    suspend fun register(@Body nurse: Nurse): Response<Nurse>

    @GET("${BASE_PATH}nurses")
    suspend fun getAllNurses(): Response<List<Nurse>>

    @GET("${BASE_PATH}nurses/{name}/name")
    suspend fun getNurseByName(@Path("name") name: String): Response<Nurse>

    @GET("${BASE_PATH}nurses/{id}")
    suspend fun getNurseById(@Path("id") id: Int): Response<Nurse>

    @PUT("${BASE_PATH}nurses/{id}")
    suspend fun updateNurse(@Path("id") id: Int, @Body nurse: Nurse): Response<Nurse>

    @PUT("${BASE_PATH}nurses/{id}/profile")
    suspend fun updateNurseProfile(@Path("id") id: Int, @Body nurse: Nurse): Response<Nurse>

    @DELETE("${BASE_PATH}nurses/{id}")
    suspend fun deleteNurse(@Path("id") id: Int): Response<String>

    @Streaming
    @POST("${BASE_PATH}nurses/{id}/upload-image")
    suspend fun uploadNurseImage(@Path("id") id: Int): Response<ResponseBody>

    @Streaming
    @GET("${BASE_PATH}nurses/{id}/image")
    suspend fun getNurseImage(@Path("id") id: Int): Response<ResponseBody>

    @GET("${BASE_PATH}checkUserAvailability")
    suspend fun checkUserAvailability(@Query("user") user: String): Response<Boolean>
}
