package com.example.jetexpensesapp.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetexpensesapp.components.Udis

@Composable
fun UdiHome(viewModel: UdiViewModel = hiltViewModel()) {
    Udis(viewModel)
}