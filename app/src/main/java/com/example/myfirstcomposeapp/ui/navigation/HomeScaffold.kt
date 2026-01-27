package com.example.myfirstcomposeapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myfirstcomposeapp.R

private data class BottomItem(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector
)

@Composable
fun HomeScaffold(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    content: @Composable (padding: androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    val items = listOf(
        BottomItem(Routes.OVERVIEW, R.string.nav_overview, Icons.Filled.Home),
        BottomItem(Routes.WORK, R.string.nav_work, Icons.Filled.List),
        BottomItem(Routes.CREATE, R.string.nav_create, Icons.Filled.Add),
        BottomItem(Routes.TRACE, R.string.nav_trace, Icons.Filled.Search),
        BottomItem(Routes.USER, R.string.nav_user, Icons.Filled.Person)
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.OVERVIEW
    val showBottomBar = currentRoute != Routes.LOGIN

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        val selected = currentRoute == item.route
                        val isRecord = item.route == Routes.CREATE
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(Routes.OVERVIEW) { saveState = true }
                                }
                            },
                            colors = if (isRecord) {
                                NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.primary,
                                    unselectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            } else {
                                NavigationBarItemDefaults.colors()
                            },
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = stringResource(item.labelRes)
                                )
                            },
                            label = { androidx.compose.material3.Text(stringResource(item.labelRes)) }
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = content
    )
}
