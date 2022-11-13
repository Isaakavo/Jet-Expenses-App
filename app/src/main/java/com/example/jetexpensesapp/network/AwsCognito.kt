package com.example.jetexpensesapp.network

import com.example.jetexpensesapp.model.jwt.Auth
import com.example.jetexpensesapp.model.jwt.Jwt
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import javax.inject.Singleton

@Singleton
interface AwsCognito {
    @Headers(
        "X-Amz-Target: AWSCognitoIdentityProviderService.InitiateAuth",
        "Content-Type: application/x-amz-json-1.1"
    )
    @POST
    suspend fun getJwtToken(
        @Url base: String,
        @Body auth: Auth
    ): Jwt
}