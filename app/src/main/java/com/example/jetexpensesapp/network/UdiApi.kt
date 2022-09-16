package com.example.jetexpensesapp.network

import com.example.jetexpensesapp.model.Bmx
import com.example.jetexpensesapp.utils.Constants.BASE_UDI_ID
import com.example.jetexpensesapp.utils.Constants.BMX_TOKEN
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface UdiApi {
    @Headers("Bmx-Token: $BMX_TOKEN")
    @GET("$BASE_UDI_ID/datos/{initialDate}/{finalDate}")
    suspend fun getUdiForToday(
        @Path("initialDate") initialDate: String,
        @Path("finalDate") finalDate: String
    ): Bmx
}