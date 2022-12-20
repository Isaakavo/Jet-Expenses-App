package com.example.jetexpensesapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetexpensesapp.components.shared.Button
import kotlinx.coroutines.launch

@Composable
fun Login(
    username: String,
    password: String,
    isLoading: Boolean,
    shouldShowPassword: Boolean = false,
    onToggleShowPassword: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RetirementInputText(text = username, label = "email", onTextChange = onUsernameChange)
        RetirementInputText(text = password,
            label = "password",
            onTextChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            visualTransformation = if (shouldShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (shouldShowPassword) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (shouldShowPassword) "Hide password" else "Show password"

                IconButton(onClick = onToggleShowPassword) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            })

        Button(onClick = onLogin, text = "Login", isLoading = isLoading)
    }
}