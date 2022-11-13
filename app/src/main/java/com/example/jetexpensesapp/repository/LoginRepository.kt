package com.example.jetexpensesapp.repository

import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.model.jwt.Auth
import com.example.jetexpensesapp.model.jwt.Jwt
import com.example.jetexpensesapp.network.AwsCognito
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val api: AwsCognito
) {
    suspend fun login(auth: Auth): Result<Jwt> = withContext(Dispatchers.IO) {
        try {
            val result = api.getJwtToken("https://cognito-idp.us-east-2.amazonaws.com/", auth)
            return@withContext Result.Success(result)
        } catch (e: Exception) {
            return@withContext Result.Error(e.message)
        }
    }

}