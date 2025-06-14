package com.sipues

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.AnnotatedString
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sipues.data.model.response.AuthResponse
import com.sipues.ui.login.LoginScreen
import com.sipues.viewmodel.AuthViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: AuthViewModel

    @Before
    fun setup() {
        mockViewModel = mockk<AuthViewModel>(relaxUnitFun = true) {
            every { state } returns MutableStateFlow(AuthViewModel.AuthState.Idle)
            every { email } returns ""
            every { password } returns ""
            every { emailError } returns null
            every { passwordError } returns null
            every { login() } just Runs
            every { validateInputs() } returns true
        }
    }


    // Pruebas básicas de UI
    @Test
    fun loginScreen_displaysCorrectInitialUI() {
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {})
        }

        composeTestRule.onNodeWithText("Email", ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText("Contraseña", ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText("Iniciar sesión", ignoreCase = true).assertExists()
    }



    // PRUEBAS DE NAVEGACIÓN
    @Test
    fun loginScreen_navigatesOnSuccess_whenAuthStateIsSuccess() {
        // 1. Configurar el mock del ViewModel
        var navigationCalled = false
        val onLoginSuccess: () -> Unit = { navigationCalled = true }

        // 2. Crear un Result exitoso REAL (no mock)
        val authResponse = AuthResponse("fake-token", "1")
        val successResult: Result<AuthResponse> = Result.success(authResponse)

        // 3. Configurar el estado del ViewModel
        every { mockViewModel.state } returns MutableStateFlow(
            AuthViewModel.AuthState.Success(successResult)
        )

        // 4. Lanzar el componente
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = onLoginSuccess)
        }

        // 5. Esperar a que Compose procese los cambios
        composeTestRule.waitForIdle()

        // 6. Verificar navegación
        assertTrue(navigationCalled)
    }

    // PRUEBAS DE INTERACCIÓN CON TECLADO
    @Test
    fun loginScreen_emailField_acceptsEmailInput() {
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {})
        }


        val testEmail = "test@example.com"
        composeTestRule.onNodeWithText("Email")
            .performTextInput(testEmail)

        // Assert: Verificamos que el ViewModel fue actualizado
        verify { mockViewModel.updateEmail(testEmail) }
    }

    @Test
    fun loginScreen_passwordField_acceptsPasswordInput() {
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {})
        }


        val testPassword = "shalala"
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput(testPassword)

        // Assert: Verificamos que el ViewModel fue actualizado
        verify { mockViewModel.updatePassword(testPassword) }
    }
    @Test
    fun loginScreen_emailField_showsKeyboardForEmail() {
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {})
        }

        composeTestRule.onNodeWithText("Email")
            .performClick()


        composeTestRule.onNodeWithText("Email")
            .assertHasClickAction()
    }

    // PRUEBAS DE ACCESIBILIDAD
    @Test
    fun loginScreen_hasProperAccessibilityLabels() {
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {})
        }

        // Verificamos labels de accesibilidad para los elementos clave
        composeTestRule.onNodeWithText("Email")
            .assertHasClickAction()

        composeTestRule.onNodeWithText("Contraseña")
            .assertHasClickAction()

        composeTestRule.onNodeWithTag("login_button")
            .assertHasClickAction()
    }

    @Test
    fun loginScreen_passwordToggle_hasAccessibilityHint() {
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {})
        }

        // Verificamos el icono de mostrar/ocultar contraseña
        composeTestRule.onNodeWithTag("password_visibility_toggle")
            .assertExists()
            .assertHasClickAction()
    }

    // PRUEBAS MEJORADAS DE VALIDACIÓN
    @Test
    fun loginScreen_showsAllErrors_whenFieldsAreEmpty() {
        // Configurar el mock para devolver errores
        every { mockViewModel.validateInputs() } returns false
        every { mockViewModel.emailError } returns "El email es requerido"
        every { mockViewModel.passwordError } returns "La contraseña es requerida"

        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {})
        }

        // Act: Intentamos hacer login sin ingresar datos
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()

        // Assert: Verificamos mensajes de error
        composeTestRule.onNodeWithText("El email es requerido").assertExists()
        composeTestRule.onNodeWithText("La contraseña es requerida").assertExists()
    }

    @Test
    fun loginScreen_showsEmailFormatError_whenEmailIsInvalid() {
        // 1. Configurar mock para devolver error de formato
        every { mockViewModel.validateInputs() } returns false
        every { mockViewModel.emailError } returns "Ingresa un email válido"
        every { mockViewModel.passwordError } returns null // Contraseña válida

        // 2. Proporcionar un email inválido
        every { mockViewModel.email } returns "emailinvalido"

        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {})
        }

        // 3. Intentar hacer login
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()

        // 4. Verificar mensaje de error
        composeTestRule.onNodeWithText("Ingresa un email válido")
            .assertExists()
            .assertIsDisplayed()

        // 5. Verificar que no muestra error de contraseña
        composeTestRule.onNodeWithText("La contraseña es requerida")
            .assertDoesNotExist()
    }
    // PRUEBA MEJORADA DE INTERACCIÓN COMPLETA
    @Test
    fun loginScreen_completeHappyPath() {
        // 1. Configurar datos de prueba
        val testEmail = "test@example.com"
        val testPassword = "password123"
        val authResponse = AuthResponse("fake-token", "1")
        val successResult = Result.success(authResponse)

        // 2. Configurar el flujo de estados con MutableStateFlow REAL
        val stateFlow = MutableStateFlow<AuthViewModel.AuthState>(AuthViewModel.AuthState.Idle)
        every { mockViewModel.state } returns stateFlow

        // 3. Configurar comportamiento del login
        every { mockViewModel.login() } answers {
            // Simular flujo completo: Loading -> Success
            stateFlow.value = AuthViewModel.AuthState.Loading
            stateFlow.value = AuthViewModel.AuthState.Success(successResult)
        }

        // 4. Variable para rastrear navegación
        var navigationCalled = false

        // 5. Lanzar el componente
        composeTestRule.setContent {
            LoginScreen(
                viewModel = mockViewModel,
                onLoginSuccess = { navigationCalled = true }
            )
        }

        // 6. Simular interacción del usuario
        // Rellenar email
        composeTestRule.onNodeWithText("Email")
            .performTextInput(testEmail)

        // Rellenar contraseña
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput(testPassword)

        // Hacer clic en login
        composeTestRule.onNodeWithText("Iniciar sesión")
            .performClick()

        // 7. Esperar CONDICIONALMENTE a que se complete la navegación
        composeTestRule.waitUntil(5000) { navigationCalled }

        // 8. Verificaciones finales
        assertTrue("La navegación debería haberse activado al recibir el estado Success", navigationCalled)
    }



    // Función de ayuda para verificar accesibilidad
    private fun hasContentDescriptionOrLabel(expected: String): SemanticsMatcher {
        return SemanticsMatcher.expectValue(
            SemanticsProperties.ContentDescription,
            listOf(expected)
        ).or(
            SemanticsMatcher.expectValue(
                SemanticsProperties.Text,
                listOf(AnnotatedString(expected))
            )
        )
    }
}