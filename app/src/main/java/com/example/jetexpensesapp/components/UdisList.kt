package com.example.jetexpensesapp.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.jetexpensesapp.screen.udis.UdiViewModel

@Composable
fun UdisList(viewModel: UdiViewModel) {
    val udisObj = viewModel.dataFromDb.collectAsState().value
    LazyColumn {
        items(udisObj) { udiObj ->
            UdiRow(udiObj)
        }
    }
}