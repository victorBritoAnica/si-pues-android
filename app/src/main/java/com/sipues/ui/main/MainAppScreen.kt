package com.sipues.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sipues.R
import com.sipues.navigation.Home
import com.sipues.ui.components.drawer.AppDrawer
import com.sipues.ui.home.HomeScreen
import com.sipues.ui.login.LoginScreen
import com.sipues.ui.search.SearchScreen
import com.sipues.viewmodel.AuthViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    onNavigateToSearch: () -> Unit,
    onLogout: () -> Unit,
    onLogin:() -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isAuthenticated by remember { derivedStateOf { authViewModel.isAuthenticated } }

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
                    currentScreen = Home,
                    onHomeSelected = { scope.launch { drawerState.close() } },
                    onSearchSelected = {
                        onNavigateToSearch()
                        scope.launch { drawerState.close() }
                    },
                    onLogout = {
                        onLogout()
                        scope.launch { drawerState.close() }
                    },
                    onLogin = {
                        onLogin()
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
                                .height(40.dp) // Ajusta el tamaño según necesites
                                .padding(start = 8.dp) // Espaciado opcional
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.vago_primary),
                        titleContentColor = colorResource(R.color.text_light),
                        actionIconContentColor = colorResource(R.color.morelos_accent)
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = colorResource(R.color.text_light) // Usa el color claro definido
                            )
                        }
                    }
                )
            }
        ) {
            HomeScreen()
        }
    }
}
