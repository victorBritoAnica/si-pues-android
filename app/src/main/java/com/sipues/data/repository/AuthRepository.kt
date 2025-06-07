package com.sipues.data.repository

import com.sipues.data.model.request.AuthRequest
import com.sipues.data.model.request.RegisterRequest
import com.sipues.data.model.response.AuthResponse
import com.sipues.data.network.AuthApiService
import com.sipues.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: AuthApiService,
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(AuthRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<Unit> {
        return try {
            val response = apiService.register(RegisterRequest(username, email, password))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}