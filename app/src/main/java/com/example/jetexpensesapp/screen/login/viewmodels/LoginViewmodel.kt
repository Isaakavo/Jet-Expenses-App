package com.example.jetexpensesapp.screen.login.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.model.jwt.Auth
import com.example.jetexpensesapp.model.jwt.AuthParameters
import com.example.jetexpensesapp.navigation.UdiScreens
import com.example.jetexpensesapp.repository.SessionRepository
import com.example.jetexpensesapp.repository.UdiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val username: String = "isaakhaas96@gmail.com",
    val password: String = "Weisses9622!",
    val isLoading: Boolean = false
)

@HiltViewModel
class LoginViewmodel @Inject constructor(
    private val repository: SessionRepository,
    private val udiRepository: UdiRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _shouldNav = MutableStateFlow("")
    val shouldNav: StateFlow<String> = _shouldNav.asStateFlow()

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

    private fun updateShouldNav(newValue: String) {
        _shouldNav.value = newValue
    }

    //TODO make a logic in the server to extract the username and email to
    //place it in the profile section
    fun attemptLogin() {
        val auth = Auth(
            AuthParameters = AuthParameters(
                PASSWORD = uiState.value.password,
                USERNAME = uiState.value.username
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = repository.login(auth)
            if (result is Result.Success) {
                val isCompleted = repository.setAuthJwtToken(result.data)
                if (isCompleted) {
                    when (val commissionResponse = udiRepository.getCommission()) {
                        is Result.Success -> {
                            updateShouldNav(UdiScreens.UDI_HOME_SCREEN)
                        }
                        is Result.Error -> {
                            if (commissionResponse.exception == "No data") {
                                updateShouldNav(UdiScreens.ADD_EDIT_COMMISSIONS_SCREEN)
                            }
                        }
                    }
                }
                //updateShouldNav(isCompleted)
            } else {
                Log.d("JWT", "Error")
            }
        }
    }
}