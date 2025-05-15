package com.sipues.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sipues.viewmodel.BusinessViewModel

@Composable
fun HomeScreen(viewModel: BusinessViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    // Cargar los datos al entrar
    LaunchedEffect(Unit) {
        viewModel.loadBusiness()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is BusinessViewModel.BusinessState.Loading -> {
                CircularProgressIndicator(color = Color.White)
            }
            is BusinessViewModel.BusinessState.Success -> {
                val businessList = (state as BusinessViewModel.BusinessState.Success).businessList
                businessList.forEach { business ->
                    Text("Nombre del negocio: ${business.name}")
                }
            }
            is BusinessViewModel.BusinessState.Error -> {
                val errorMessage = (state as BusinessViewModel.BusinessState.Error).message
                Text("Error: $errorMessage")
            }
        }
    }
}