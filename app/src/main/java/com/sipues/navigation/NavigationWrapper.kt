package com.sipues.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sipues.ui.home.HomeScreen
import com.sipues.ui.login.LoginScreen
import com.sipues.ui.main.MainAppScreen
import com.sipues.ui.search.SearchScreen
import com.sipues.viewmodel.AuthViewModel

@Composable
fun NavigationWrapper () {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Home){

        composable<Login> {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate(Home) }
            )
        }

        composable<Home> {
            MainAppScreen(
                onNavigateToSearch = { navController.navigate(SearchScreen) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Login) {
                        popUpTo(Home) { inclusive = true }
                    }
                },
                onLogin = {
                    navController.navigate(Login) {
                        popUpTo(Home) { inclusive = true }
                    }
                }
            )
        }

        composable<SearchScreen> {
            SearchScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}