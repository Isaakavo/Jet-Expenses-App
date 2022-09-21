package com.example.jetexpensesapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UdiRowAddEntry(
    title: String,
    content: String,
    fontSize: TextUnit = 18.sp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Surface(
            Modifier
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(6.dp))
                .fillMaxWidth(),
            elevation = 12.dp
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1,
                    fontSize = fontSize,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .fillMaxWidth(0.5f)
                )
                Text(
                    text = content,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                    fontSize = fontSize,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    UdiRowAddEntry("Total comprado", "3453.05")
}