package com.sipues.ui.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sipues.R
import com.sipues.navigation.Routes
import com.sipues.viewmodel.BusinessViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: BusinessViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Image(
            painter = painterResource(id = R.drawable.si_pues),
            contentDescription = "Logo de la app",
            modifier = Modifier
                .align(Alignment.Center)
                .size(500.dp)
        )

        // Loading animado
        CircularProgressIndicator(
            color = Color(0xFF4527A0),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
        )

        LaunchedEffect(Unit) {
            viewModel.syncBusiness() // Iniciar sincronización

            viewModel.syncState.collect { state ->
                when (state) {
                    BusinessViewModel.SyncState.Success -> {
                        Log.d("Exito", "se hizo la sync de manera exitosa")
                        navController.navigate(Routes.HOME) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                    is BusinessViewModel.SyncState.Error -> {
                        Log.e("SyncError",  state.message)
                        navController.navigate(Routes.HOME)
                    }
                    else -> { Log.d("Loading", "Cargando") }
                }
            }
        }
    }
}