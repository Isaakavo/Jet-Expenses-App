package com.example.jetexpensesapp.model.jwt

data class Jwt(
    val AuthenticationResult: AuthenticationResult,
    val ChallengeParameters: ChallengeParameters
)

data class AuthenticationResult(
    val AccessToken: String,
    val ExpiresIn: Int,
    val IdToken: String,
    val RefreshToken: String,
    val TokenType: String
)

class ChallengeParameters