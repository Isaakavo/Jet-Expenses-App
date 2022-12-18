package com.example.jetexpensesapp.model.jwt

import com.example.jetexpensesapp.utils.Constants


data class Auth(
    val AuthFlow: String = "USER_PASSWORD_AUTH",
    val AuthParameters: AuthParameters,
    val ClientId: String = Constants.CLIENT_ID
)

data class AuthParameters(
    val PASSWORD: String,
    val USERNAME: String
)