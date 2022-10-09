package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetexpensesapp.data.UdiGlobalDetails
import com.example.jetexpensesapp.utils.formatMoney
import com.example.jetexpensesapp.utils.formatNumber

@Composable
fun GlobalDetail(
    udisGlobalDetails: UdiGlobalDetails,
    onAddEntry: () -> Unit = {}
    // navController: NavController = rememberNavController()
) {
    Column(modifier = Modifier.clickable(
        interactionSource = remember {
            MutableInteractionSource()
        },
        indication = null
    ) {
        //navController.navigate(Screen.UdiGlobalDetailsScreen.route)
    }) {
        Row() {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 7.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalle Global",
                        textAlign = TextAlign.Left,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h1,
                    )
                    Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null)
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
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = formatMoney(udisGlobalDetails.totalExpend),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.weight(0.3f),
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = formatNumber(udisGlobalDetails.udisTotal),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.weight(0.3f),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                text = "Agregar",
                shape = CircleShape,
                onClick = onAddEntry)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GlobalDetailPreview() {
    //GlobalDetail()
}