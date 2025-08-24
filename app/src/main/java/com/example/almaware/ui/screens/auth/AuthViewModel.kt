package com.example.almaware.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almaware.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    var acceptPolicies by mutableStateOf(false)

    var authState by mutableStateOf<AuthState>(AuthState.Idle)

    fun signIn() {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                authRepository.signIn(email, password)
                authState = AuthState.Success
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                authRepository.signUp(name, email, password)
                authState = AuthState.Success
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}