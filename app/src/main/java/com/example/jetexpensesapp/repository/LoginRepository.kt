package com.example.jetexpensesapp.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.model.jwt.Auth
import com.example.jetexpensesapp.network.AwsCognito
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val api: AwsCognito
) {

    private val pref: SharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun setLoggedIn(key: String, isLoggedIn: Boolean) {
        editor.putBoolean(key, isLoggedIn)
        editor.commit()
    }

    fun setAuthJwtToken(key: String, jwt: String) {
        editor.putString(key, jwt)
        editor.commit()
    }

    fun getAuthJwtToken(key: String): String? {
        return pref.getString(key, "")
    }

    suspend fun login(auth: Auth): Result<String> = withContext(Dispatchers.IO) {
        try {
            val result = api.getJwtToken(
                "https://cognito-idp.us-east-2.amazonaws.com/",
                auth
            ).AuthenticationResult.AccessToken
            return@withContext Result.Success(result)
        } catch (e: Exception) {
            return@withContext Result.Error(e.message)
        }
    }

}