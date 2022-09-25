package com.example.jetexpensesapp.screen.udis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetexpensesapp.components.RetirementButton
import com.example.jetexpensesapp.components.UdiListModalSheet
import com.example.jetexpensesapp.navigation.Screen

@Composable
fun UdiHomeScreen(navController: NavController, viewModel: UdiViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 15.dp)
        ) {
            RetirementButton(text = "Agregar entrada",
                modifier = Modifier.clip(RoundedCornerShape(35.dp)),
                onClick = {
                    navController.navigate(Screen.AddRetirementEntryScreen.route)
                })
        }
        Card(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            elevation = 5.dp
        ) {
            UdiListModalSheet(viewModel = viewModel)
        }

    }
}
