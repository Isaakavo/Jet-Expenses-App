package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun UdiValueDisplay(
    udiValueToday: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Precio de la udi Hoy: $${udiValueToday}",
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(end = 10.dp),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
    }
}