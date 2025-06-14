package com.sipues.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sipues.data.repository.AuthRepository
import com.sipues.data.model.response.AuthResponse
import com.sipues.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    sealed interface AuthState {
        object Idle : AuthState
        object Loading : AuthState
        data class Success(val authResponse: Result<AuthResponse>) : AuthState
        data class Error(val message: String) : AuthState
    }

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    val isAuthenticated: Boolean
        get() = tokenManager.getToken() != null
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set

    fun updateEmail(newEmail: String) {
        email = newEmail
        emailError = null
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        passwordError = null
    }

    fun validateInputs(): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            emailError = "El email es requerido"
            isValid = false
        } else if (!isValidEmail(email)) {
            emailError = "Ingresa un email válido"
            isValid = false
        }

        if (password.isEmpty()) {
            passwordError = "La contraseña es requerida"
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailPattern.matcher(email).matches()
    }

    fun login() {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val response = authRepository.login(email, password)
                if (response.isSuccess) {
                    response.getOrNull()?.let {
                        tokenManager.saveToken(it.token)
                        _state.value = AuthState.Success(response)
                    }
                } else {
                    _state.value = AuthState.Error(
                        message = response.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            } catch (e: Exception) {
                _state.value = AuthState.Error(
                    message = e.message ?: "Error de conexión"
                )
            }
        }
    }

    // Función de registro (descomenta cuando la necesites)
    /*
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                repository.register(username, email, password)
                _state.value = AuthState.Success(Unit)
            } catch (e: Exception) {
                _state.value = AuthState.Error(
                    message = e.message ?: "Error desconocido al registrarse"
                )
            }
        }
    }
    */

    fun logout() {
        tokenManager.clearToken()
    }
}