package com.sipues

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sipues.ui.login.LoginScreen
import com.sipues.viewmodel.AuthViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeStateFlow = MutableStateFlow<AuthViewModel.AuthState>(AuthViewModel.AuthState.Loading)

    private val mockViewModel = mockk<AuthViewModel>(relaxed = true)

    @Before
    fun setup() {
        // Cuando se pida el state, devolver el MutableStateFlow real
        every { mockViewModel.state } returns fakeStateFlow
        // También puedes mockear email y password si los usas en tu UI
        every { mockViewModel.email } returns ""
        every { mockViewModel.password } returns ""
    }

    @Test
    fun loginScreen_showsErrorOnFailedLogin() {
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {})
        }

        // Simular que el usuario escribe email y password
        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("wrong")

        // Simular click en login
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()

        // Verificar que se llamó a login() en el ViewModel mockeado
        verify { mockViewModel.login() }

        // Simular que el login falla y el estado cambia a Error
        fakeStateFlow.value = AuthViewModel.AuthState.Error("Credenciales incorrectas")

        // Verificar que el mensaje de error aparece en la UI
        composeTestRule.onNodeWithText("Credenciales incorrectas").assertExists()
    }
}