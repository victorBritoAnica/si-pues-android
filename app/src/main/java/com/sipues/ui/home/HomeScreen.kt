package com.sipues.ui.home


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.sipues.viewmodel.BusinessViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sipues.ui.components.BusinessCard

@Composable
fun HomeScreen(
    viewModel: BusinessViewModel = hiltViewModel(),
) {
    val state by viewModel.businessState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBusiness()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (state) {
            is BusinessViewModel.BusinessState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is BusinessViewModel.BusinessState.Success -> {
                val businesses = (state as BusinessViewModel.BusinessState.Success).businessList

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(businesses) { business ->
                        BusinessCard(business = business)
                    }
                }
            }

            is BusinessViewModel.BusinessState.Error -> {
                val message = (state as BusinessViewModel.BusinessState.Error).message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: $message",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> Unit
        }
    }
}