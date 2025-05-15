package com.sipues.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sipues.ui.home.HomeScreen
import com.sipues.ui.login.LoginScreen

@Composable
fun NavigationWrapper () {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Home){

        composable<Login> {
           LoginScreen()
        }

        composable<Home> {
            HomeScreen()
        }
    }
}