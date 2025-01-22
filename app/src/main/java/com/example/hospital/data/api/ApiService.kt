package com.example.hospital.data.api

import retrofit2.Response
import retrofit2.http.*

// ApiService
interface RemoteNurseApi {

    // TEST
    @GET("nurse/directory")
    suspend fun getRemoteNurse(): Response<List<Nurse>>

    // POST /authentication -> login()
    @POST("nurse/authentication")
    suspend fun login(@Body nurse: Nurse): Response<Nurse>

    // POST /registration -> register()
    @POST("nurse/registration")
    suspend fun register(@Body nurse: Nurse): Response<Nurse>

    // GET /directory -> getNurseDirectory()
    @GET("nurse/directory")
    suspend fun getNurseDirectory(): Response<List<Nurse>>

    // GET /search-by-name/{name} -> searchNursesByName()
    @GET("nurse/search-by-name/{name}")
    suspend fun searchNursesByName(@Path("name") name: String): Response<Nurse>

    // GET /profile/{id} -> getProfile()
    @GET("nurse/profile/{id}")
    suspend fun getProfile(@Path("id") id: Int): Response<Nurse>

    // PUT /modification/{id} -> updateNurse()
    @PUT("nurse/modification/{id}")
    suspend fun updateNurse(@Path("id") id: Int, @Body nurse: Nurse): Response<Nurse>

    // DELETE /deletion/{id} -> deleteNurse()
    @DELETE("nurse/deletion/{id}")
    suspend fun deleteNurse(@Path("id") id: Int): Response<String>
}
