package com.example.jetexpensesapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetexpensesapp.screen.udis.AddRetirementEntryScreen
import com.example.jetexpensesapp.screen.udis.UdiHomeScreen
import com.example.jetexpensesapp.screen.udis.UdiViewModel

@Composable
fun ExpensesNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.UdiHomeScreen,
//        Screen.AddRetirementEntryScreen
    )
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { screen.icon?.let { Icon(it, contentDescription = null) } },
                        label = { Text(text = stringResource(id = screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.UdiHomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screen.UdiHomeScreen.route) {
                val udiViewModel = hiltViewModel<UdiViewModel>()
                UdiHomeScreen(navController, viewModel = udiViewModel)
            }

            composable(Screen.AddRetirementEntryScreen.route) {
                val udiViewModel = hiltViewModel<UdiViewModel>()
                AddRetirementEntryScreen(navController, viewModel = udiViewModel)
            }
        }
    }

}