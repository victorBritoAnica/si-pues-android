package com.sipues

import com.sipues.data.model.response.AuthResponse
import com.sipues.data.repository.AuthRepository
import com.sipues.utils.TokenManager
import com.sipues.viewmodel.AuthViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private val authRepository = mockk<AuthRepository>()
    private val tokenManager = mockk<TokenManager>(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(authRepository, tokenManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success updates token and state`() = runTest {
        // Mock
        val mockAuthResponse = AuthResponse(token = "fake_token", userId = "2")
        coEvery { authRepository.login(any(), any()) } returns Result.success(mockAuthResponse)

        // Ejecutar
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("1234")
        viewModel.login()

        // Esperar que se ejecuten las corrutinas
        advanceUntilIdle()

        // Verificar
        verify { tokenManager.saveToken("fake_token") }
        assert(viewModel.state.value is AuthViewModel.AuthState.Success)
    }

    @Test
    fun `login failure updates error state`() = runTest {
        // Mock
        coEvery { authRepository.login(any(), any()) } returns Result.failure(Exception("Invalid credentials"))

        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("wrong-password")
        viewModel.login()

        // Esperar que se ejecuten las corrutinas
        advanceUntilIdle()

        // Verificar
        val state = viewModel.state.value as AuthViewModel.AuthState.Error
        assertEquals("Invalid credentials", state.message)
    }
}