package com.sipues.ui.components.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sipues.navigation.Routes

@Composable
fun AppDrawer(
    currentScreen: String,
    onHomeSelected: () -> Unit,
    onSearchSelected: () -> Unit,
    onLogout: () -> Unit,
    isAuthenticated: Boolean,
    onLogin: () -> Unit = {},
    closeDrawer: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(35.dp))

        DrawerItem(
            icon = Icons.Default.Home,
            label = "Inicio",
            selected = currentScreen == Routes.HOME,
            onItemClick = {
                onHomeSelected()
                closeDrawer()
            }
        )

        DrawerItem(
            icon = Icons.Default.Search,
            label = "Buscar",
            selected = currentScreen == Routes.SEARCH,
            onItemClick = {
                onSearchSelected()
                closeDrawer()
            }
        )

        if (isAuthenticated) {
            DrawerItem(
                icon = Icons.Default.ExitToApp,
                label = "Cerrar sesión",
                selected = false,
                onItemClick = {
                    onLogout()
                    closeDrawer()
                }
            )
        } else {
            DrawerItem(
                icon = Icons.Default.Login,
                label = "Iniciar sesión",
                selected = false,
                onItemClick = {
                    onLogin()
                    closeDrawer()
                }
            )
        }

    }
}