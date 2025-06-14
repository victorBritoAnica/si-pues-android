package com.sipues.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sipues.R
import com.sipues.navigation.AppNavHost
import com.sipues.navigation.Routes
import com.sipues.ui.components.ConfirmationDialog
import com.sipues.ui.components.drawer.AppDrawer
import kotlinx.coroutines.launch
import com.sipues.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isAuthenticated by authViewModel.isAuthenticated
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.logout_confirmation_title),
            message = stringResource(R.string.logout_confirmation_message),
            onConfirm = {
                authViewModel.logout()
                navController.navigate(Routes.HOME) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
                scope.launch { drawerState.close() }
                showLogoutDialog = false
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Transparent,
        drawerContent = {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(190.dp),
                color = colorResource(R.color.background_light),
                shadowElevation = 8.dp
            ) {

                    AppDrawer(
                        currentScreen = Routes.HOME,
                        onHomeSelected = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            scope.launch { drawerState.close() }
                        },
                        onSearchSelected = {
                            navController.navigate(Routes.SEARCH)
                            scope.launch { drawerState.close() }
                        },
                        onLogout = {
                            showLogoutDialog = true
                            scope.launch { drawerState.close() }
                        },
                        onLogin = {
                            navController.navigate(Routes.LOGIN)
                            scope.launch { drawerState.close() }
                        },
                        isAuthenticated = isAuthenticated
                    )

            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.si_pues),
                            contentDescription = "Logo Si Pues",
                            modifier = Modifier
                                .height(40.dp)
                                .padding(start = 8.dp)
                                .clip(CircleShape)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.vago_primary),
                        titleContentColor = colorResource(R.color.text_light),
                        actionIconContentColor = colorResource(R.color.morelos_accent)
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = colorResource(R.color.text_light)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}