package com.example.jetexpensesapp.screen.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetexpensesapp.components.Login
import com.example.jetexpensesapp.screen.login.viewmodels.LoginViewmodel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LoginScreen(
    navigate: (Boolean) -> Unit,
    viewmodel: LoginViewmodel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    Login(
        username = uiState.username,
        password = uiState.password,
        onUsernameChange = viewmodel::updateUsername,
        onPasswordChange = viewmodel::updatePassword
    ) {
        viewmodel.attemptLogin()
        navigate(uiState.shouldNav)
    }
}