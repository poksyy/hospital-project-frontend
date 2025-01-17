package com.example.hospital.data.api

import retrofit2.Response
import retrofit2.http.*

data class Nurse(val name: String = "", val age: Int)

interface RemoteNurseApi {
    @GET("/nurse/directory")
    suspend fun getRemoteNurse(): Response<List<Nurse>>

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
