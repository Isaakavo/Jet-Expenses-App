package com.example.jetexpensesapp.network.interceptor

import android.util.Log
import com.example.jetexpensesapp.repository.SessionRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor() : Interceptor {

    var sessionToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        if (request.header(SessionRepository.AUTH_KEY) == null) {
            if (sessionToken == null) {
                Log.d("Interceptor", "The token is null")
            } else {
                sessionToken.let {
                    requestBuilder.addHeader("Authorization", "Bearer $it")
                }

            }
        }

        return chain.proceed(requestBuilder.build())
    }

}