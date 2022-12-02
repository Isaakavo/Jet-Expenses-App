package com.example.jetexpensesapp.network

import com.example.jetexpensesapp.model.udi.RetirementRecord
import com.example.jetexpensesapp.model.udi.ServerResponse
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface UdiEndpoint {
    @GET("api/udis")
    suspend fun getAllUdis(): ServerResponse

    @GET("api/udis/{id}")
    suspend fun getUdiById(@Path("id") id: Long): ServerResponse?

    @POST("api/udis")
    suspend fun insertUdi(@Body retirementRecord: RetirementRecord): ServerResponse

    @PUT("api/udis/{id}")
    suspend fun updateUdi(@Path("id") id: Long, @Body retirementRecord: RetirementRecord): ServerResponse

    @DELETE("api/udis/{id}")
    suspend fun deleteUdi(@Path("id") id: Long): ServerResponse

    @GET("api/udis/commission")
    suspend fun getCommission(): ServerResponse
}