package com.example.jetexpensesapp.network

import com.example.jetexpensesapp.model.udi.ServerResponse
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface UdiEndpoint {
    @GET("api/udis")
    suspend fun getAllUdis(): ServerResponse
}