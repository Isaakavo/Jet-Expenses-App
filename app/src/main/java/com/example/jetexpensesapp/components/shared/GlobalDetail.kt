package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetexpensesapp.components.RetirementButton

@Composable
fun GlobalDetail() {
    Column() {
        Row() {
            Card(
                //modifier = Modifier.padding(5.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 7.dp)
                    ) {
                        Text(
                            text = "Detalle Global",
                            textAlign = TextAlign.Left,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.h1,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Total Gastado",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .weight(0.3f)
                                .padding(start = 4.dp, end = 4.dp)
                        )
                        Text(
                            text = "Total de Udis",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .weight(0.3f)
                                .padding(start = 4.dp, end = 4.dp)
                        )
                        Text(
                            text = "Conversion",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .weight(0.3f)
                                .padding(start = 4.dp, end = 4.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$10000",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.weight(0.3f),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "70000",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.weight(0.3f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$120981293",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.weight(0.3f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            RetirementButton(text = "Agregar", onClick = { /*TODO*/ })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GlobalDetailPreview() {
    GlobalDetail()
}