package com.example.jetexpensesapp.screen.login.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.model.jwt.Auth
import com.example.jetexpensesapp.model.jwt.AuthParameters
import com.example.jetexpensesapp.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val username: String = "isaakhaas96@gmail.com",
    val password: String = "Weisses9622!",
    val shouldNav: Boolean = false
)

@HiltViewModel
class LoginViewmodel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUsername(newValue: String) {
        _uiState.update {
            it.copy(username = newValue)
        }
    }

    fun updatePassword(newValue: String) {
        _uiState.update {
            it.copy(password = newValue)
        }
    }

    private fun updateShouldNav(newValue: Boolean) = _uiState.update {
        it.copy(shouldNav = newValue)
    }

    fun attemptLogin() {
        val auth = Auth(
            AuthParameters = AuthParameters(
                PASSWORD = uiState.value.password,
                USERNAME = uiState.value.username
            )
        )
        viewModelScope.launch {
            val result = repository.login(auth)
            if (result is Result.Success) {
                val isCompleted = repository.setAuthJwtToken("AUTH_KEY", result.data)
                updateShouldNav(isCompleted)
                Log.d("JWT", "Value $result")
            } else {
                Log.d("JWT", "Error")
            }
        }
    }
}