package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DateRow(
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(5.dp)
    ) {
        Text(
            text = date,
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Right,
            modifier = modifier.fillMaxWidth()
        )
    }
}