package com.example.jetexpensesapp.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewmodel: LoginViewmodel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Hola",
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h1
                )
                Text(
                    text = "Inicia Sesión!",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 5.dp),
                    style = MaterialTheme.typography.h1
                )
            }


            Login(
                username = uiState.username,
                password = uiState.password,
                isLoading = uiState.isLoading,
                shouldShowPassword = uiState.shouldShowPassword,
                onToggleShowPassword = viewmodel::updateShouldShowPassword,
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

            uiState.userMessage?.let { message ->
                LaunchedEffect(scaffoldState.snackbarHostState, viewmodel, message, message) {
                    scaffoldState.snackbarHostState.showSnackbar(message)
                    viewmodel.snackbarMessageShown()
                }
            }
        }
    }

}