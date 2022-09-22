package com.example.jetexpensesapp.screen.udis

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetexpensesapp.components.RetirementButton
import com.example.jetexpensesapp.components.UdisList
import com.example.jetexpensesapp.navigation.ExpensesScreens

@Composable
fun UdiHomeScreen(navController: NavController, viewModel: UdiViewModel = hiltViewModel()) {
    Column {
        RetirementButton(text = "Agregar entrada", onClick = {
            navController.navigate(ExpensesScreens.AddRetirementEntryScreen.name)
        })
        UdisList(viewModel)
    }
}