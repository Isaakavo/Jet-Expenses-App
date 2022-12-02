package com.example.jetexpensesapp.screen.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetexpensesapp.components.Login
import com.example.jetexpensesapp.screen.login.viewmodels.LoginViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
//TODO make navigate callback capable of receive the commissions object to pass it to the home screen
fun LoginScreen(
    navigate: (String) -> Unit,
    viewmodel: LoginViewmodel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    //TODO add logic to handle loading ui animation when login and improve the ui
    Login(
        username = uiState.username,
        password = uiState.password,
        isLoading = uiState.isLoading,
        onUsernameChange = viewmodel::updateUsername,
        onPasswordChange = viewmodel::updatePassword
    ) {
        viewmodel.attemptLogin()

        coroutineScope.launch {
            viewmodel.shouldNav.collect {
                navigate(it)
            }
        }
    }
}