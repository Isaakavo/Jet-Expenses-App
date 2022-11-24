package com.example.jetexpensesapp.network.interceptor

import android.util.Log
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

        if (request.header("AUTH_KEY") == null) {
            if (sessionToken == null) {
                Log.d("Interceptor", "The token is null")
            } else {
                sessionToken.let {
                    if (it != null) {
                        requestBuilder.addHeader("Authorization", "Bearer $it")
                    }
                }

            }
        }

        return chain.proceed(requestBuilder.build())
    }

}