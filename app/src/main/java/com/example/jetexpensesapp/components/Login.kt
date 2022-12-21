package com.example.jetexpensesapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
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
    val modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, end = 10.dp)

    Column(
        modifier = Modifier
            .padding(top = 15.dp)
    ) {
        RetirementInputText(
            text = username,
            label = "email",
            onTextChange = onUsernameChange,
            modifier = modifier
        )
        RetirementInputText(text = password,
            label = "password",
            onTextChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            modifier = modifier,
            visualTransformation =
            if (shouldShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (shouldShowPassword) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (shouldShowPassword) "Hide password" else "Show password"

                IconButton(onClick = onToggleShowPassword) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            })

        Button(
            onClick = onLogin,
            text = "Login",
            isLoading = isLoading,
            modifier = modifier.padding(top = 5.dp),
            shape = RoundedCornerShape(6.dp)
        )
    }


}