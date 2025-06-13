package com.sipues

import com.sipues.data.model.response.AuthResponse
import com.sipues.data.network.AuthApiService
import com.sipues.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

class AuthRepositoryTest {

    private val apiService = mockk<AuthApiService>()
    private val authRepository = AuthRepository(apiService)

    @Test
    fun `login success`() = runTest {
        // Mock de respuesta exitosa
        val mockResponse = Response.success(AuthResponse(token = "fake_token", userId = "2"))
        coEvery { apiService.login(any()) } returns mockResponse

        // Ejecutar
        val result = authRepository.login("test@example.com", "1234")

        // Verificar
        assertTrue(result.isSuccess)
        assertEquals("fake_token", result.getOrNull()?.token)
    }

    @Test
    fun `login failure`() = runTest {
        // Mock de error
        coEvery { apiService.login(any()) } throws Exception("Network error")

        // Ejecutar
        val result = authRepository.login("test@example.com", "1234")

        // Verificar
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}