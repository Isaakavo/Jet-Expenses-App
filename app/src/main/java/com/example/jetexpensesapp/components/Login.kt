package com.example.jetexpensesapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.jetexpensesapp.components.shared.Button

@Composable
fun Login (
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
) {

    Column() {
        RetirementInputText(text = username, label = "email", onTextChange = onUsernameChange)
        RetirementInputText(text = password, label = "password", onTextChange = onPasswordChange)
        Button(onClick = onLogin, text = "Login")
    }
}