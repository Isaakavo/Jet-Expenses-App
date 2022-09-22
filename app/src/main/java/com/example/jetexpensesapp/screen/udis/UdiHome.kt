package com.example.jetexpensesapp.screen.udis

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetexpensesapp.components.UdisList

@Composable
fun UdiHome(viewModel: UdiViewModel = hiltViewModel()) {
    Column {
        UdisList(viewModel)
    }
}