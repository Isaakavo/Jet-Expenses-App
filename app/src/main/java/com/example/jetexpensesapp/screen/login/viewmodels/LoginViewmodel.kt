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
    val password: String = "Weisses9622!"
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

    fun attemptLogin(): Boolean {
        val auth = Auth(
            AuthParameters = AuthParameters(
                PASSWORD = uiState.value.password,
                USERNAME = uiState.value.username
            )
        )
        val scope = viewModelScope.launch {
            val result = repository.login(auth)
            if (result is Result.Success) {
                repository.setAuthJwtToken("AUTH_KEY", result.data)
                Log.d("JWT", "Value $result")
            } else {
                Log.d("JWT", "Error")
            }
        }
        if (scope.isCompleted) return true
        return false
    }
}