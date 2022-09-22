package com.example.jetexpensesapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetexpensesapp.screen.udis.AddRetirementEntryScreen
import com.example.jetexpensesapp.screen.udis.UdiHomeScreen
import com.example.jetexpensesapp.screen.udis.UdiViewModel

@Composable
fun ExpensesNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ExpensesScreens.UdiHomeScreen.name) {

        composable(ExpensesScreens.UdiHomeScreen.name) {
            val udiViewModel = hiltViewModel<UdiViewModel>()
            UdiHomeScreen(navController, viewModel = udiViewModel)
        }

        composable(ExpensesScreens.AddRetirementEntryScreen.name){
            val udiViewModel = hiltViewModel<UdiViewModel>()
            AddRetirementEntryScreen(navController, viewModel = udiViewModel)
        }
    }
}