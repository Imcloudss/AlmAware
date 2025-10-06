package com.example.almaware.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almaware.data.model.User
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
    private var currentUser by mutableStateOf<User?>(null)

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        if (authRepository.isUserLoggedIn()) {
            viewModelScope.launch {
                try {
                    val user = authRepository.getCurrentUser()
                    currentUser = user
                    if (user != null) {
                        authState = AuthState.Success
                    }
                } catch (e: Exception) {
                    authState = AuthState.Error(e.message ?: "Failed to get current user")
                }
            }
        }
    }

    fun signIn() {
        if (!validateSignInInput()) return

        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val user = authRepository.signIn(email, password)
                currentUser = user
                authState = AuthState.Success
                clearFields()
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signUp() {
        if (!validateSignUpInput()) return

        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val user = authRepository.signUp(name, email, password)
                currentUser = user
                authState = AuthState.Success
                clearFields()
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.signOut()
                currentUser = null
                authState = AuthState.Idle
                clearFields()
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Sign out failed")
            }
        }
    }

    fun updateUserProfile(newUsername: String) {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val updatedUser = authRepository.updateUserProfile(userId, newUsername)
                currentUser = updatedUser
                authState = AuthState.Success
            } catch (e: Exception) {
                authState = AuthState.Error(e.message ?: "Profile update failed")
            }
        }
    }

    private fun validateSignInInput(): Boolean {
        return when {
            email.isBlank() -> {
                authState = AuthState.Error("Email cannot be empty")
                false
            }
            password.isBlank() -> {
                authState = AuthState.Error("Password cannot be empty")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                authState = AuthState.Error("Please enter a valid email")
                false
            }
            else -> true
        }
    }

    private fun validateSignUpInput(): Boolean {
        return when {
            name.isBlank() -> {
                authState = AuthState.Error("Name cannot be empty")
                false
            }
            email.isBlank() -> {
                authState = AuthState.Error("Email cannot be empty")
                false
            }
            password.length < 6 -> {
                authState = AuthState.Error("Password must be at least 6 characters")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                authState = AuthState.Error("Please enter a valid email")
                false
            }
            !acceptPolicies -> {
                authState = AuthState.Error("Please accept the terms and conditions")
                false
            }
            else -> true
        }
    }

    private fun clearFields() {
        email = ""
        password = ""
        name = ""
        acceptPolicies = false
    }

    fun clearError() {
        if (authState is AuthState.Error) {
            authState = AuthState.Idle
        }
    }

    fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()

    // Proprietà per accedere all'utente corrente
    fun getUser(): User? = currentUser
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}