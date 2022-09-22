package com.example.jetexpensesapp.screen.udis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetexpensesapp.components.RetirementButton
import com.example.jetexpensesapp.components.UdisList
import com.example.jetexpensesapp.navigation.Screen

@Composable
fun UdiHomeScreen(navController: NavController, viewModel: UdiViewModel = hiltViewModel()) {
    Column(horizontalAlignment = Alignment.End) {
        RetirementButton(text = "Agregar entrada",
            modifier = Modifier.clip(RoundedCornerShape(35.dp)),
            onClick = {
            navController.navigate(Screen.AddRetirementEntryScreen.route)
        })

        UdisList(viewModel)
    }
}
