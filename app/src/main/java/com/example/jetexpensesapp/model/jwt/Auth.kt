package com.example.jetexpensesapp.model.jwt


data class Auth(
    val AuthFlow: String = "USER_PASSWORD_AUTH",
    val AuthParameters: AuthParameters,
    val ClientId: String = "25oksgjnl258639r4cvp4nl0v2"
)

data class AuthParameters(
    val PASSWORD: String,
    val USERNAME: String
)