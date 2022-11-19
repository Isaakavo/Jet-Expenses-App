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
class SessionRepository @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val api: AwsCognito
) {

    companion object {
        const val AUTH_KEY = "AUTH_KEY"
    }

    private val pref: SharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    private fun String.put(string: String): Boolean {
        editor.putString(this, string)
        return editor.commit()
    }

    private fun String.getString() = pref.getString(this, "")

    fun setAuthJwtToken(jwt: String): Boolean {
        return AUTH_KEY.put(jwt)
    }

    fun getAuthJwtToken(): String? {
        return AUTH_KEY.getString()
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