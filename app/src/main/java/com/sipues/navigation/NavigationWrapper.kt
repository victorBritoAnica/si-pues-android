package com.sipues.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sipues.data.repository.AuthRepository
import com.sipues.ui.home.HomeScreen
import com.sipues.ui.login.LoginScreen
import com.sipues.viewmodel.AuthViewModel

@Composable
fun NavigationWrapper () {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Login){

        composable<Login> {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate(Home) }
            )
        }

        composable<Home> {
            HomeScreen()
        }
    }
}