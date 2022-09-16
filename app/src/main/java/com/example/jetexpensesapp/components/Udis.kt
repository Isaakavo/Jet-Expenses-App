package com.example.jetexpensesapp.components

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.jetexpensesapp.screen.UdiViewModel

@Composable
fun Udis(viewModel: UdiViewModel) {
    Log.d("Udi", "udi composable ${viewModel.data.value.data}")
}