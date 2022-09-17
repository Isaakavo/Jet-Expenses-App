package com.example.jetexpensesapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.components.UdisList

@Composable
fun UdiHome(viewModel: UdiViewModel = hiltViewModel()) {
    Column {
        UdisList(viewModel)
    }
}