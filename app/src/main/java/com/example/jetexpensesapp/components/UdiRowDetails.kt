package com.example.jetexpensesapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UdiRowDetails(
    title: String,
    content: String,
    fontSize: TextUnit = 16.sp
) {
    Column(
        Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(top = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            )
            Text(
                text = content,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.body2,
                fontSize = fontSize,
                modifier = Modifier.fillMaxWidth(0.5f),
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun preview() {
    UdiRowDetails("Total comprado", "3453.05")
}