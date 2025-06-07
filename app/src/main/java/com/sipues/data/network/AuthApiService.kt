package com.sipues.data.network

import com.sipues.data.model.request.AuthRequest
import com.sipues.data.model.request.RegisterRequest
import com.sipues.data.model.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

}