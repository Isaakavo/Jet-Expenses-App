package com.example.jetexpensesapp.screen.udis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jetexpensesapp.components.UdiRowDetails
import com.example.jetexpensesapp.components.shared.TopBar
import com.example.jetexpensesapp.data.UdiGlobalDetails
import com.example.jetexpensesapp.utils.formatMoney

@Composable
fun UdiGlobalDetailsScreen(
    udiGlobalDetails: UdiGlobalDetails = UdiGlobalDetails(),
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        elevation = 0.dp
    ) {
        Column {
            TopBar(
                backgroundColor = Color.Transparent,
                title = "Detalles Globales",
                onBack = onBack
            ) {
                //navController?.popBackStack()
            }
            UdiRowDetails(
                title = "Total Gastado",
                content = formatMoney(udiGlobalDetails.totalExpend)
            )
            UdiRowDetails(
                title = "Valor de la UDI",
                content = udiGlobalDetails.udiValueToday
            )
            UdiRowDetails(
                title = "Total de UDIS",
                content = String.format("%.2f", udiGlobalDetails.udisTotal)
            )
            UdiRowDetails(
                title = "Conversion",
                content = formatMoney(udiGlobalDetails.udisConvertion)
            )
            UdiRowDetails(
                title = "Rendimiento",
                content = formatMoney(
                    udiGlobalDetails.udisConvertion - udiGlobalDetails.totalExpend
                )
            )
            UdiRowDetails(
                title = "Starting date",
                content = "11 ago 2022"
            )
            UdiRowDetails(
                title = "Plazo de pago",
                content = "20 años"
            )
            if (udiGlobalDetails.udiBonus.isNotEmpty()) {
                UdiRowDetails(
                    title = "Udi Prima",
                    content = udiGlobalDetails.udiBonus[0].monthlyTotalBonus.toString()
                )
            }
        }
    }
}