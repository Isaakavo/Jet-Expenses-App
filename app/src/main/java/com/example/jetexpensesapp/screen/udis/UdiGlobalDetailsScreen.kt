package com.example.jetexpensesapp.screen.udis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                backgroundColor = Color.Transparent,
                title = "Detalles Globales",
                onBack = onBack
            )
        }
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Text(
                text = "Detalle del total gastado en tu seguro",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            CardDetails() {
                Column {
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
                }
            }
            Text(
                text = "Detalle de tu seguro",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            CardDetails() {
                Column {
                    UdiRowDetails(
                        title = "Fecha de inicio",
                        content = "11 ago 2022"
                    )
                    UdiRowDetails(
                        title = "Plazo de pago",
                        content = "20 años"
                    )
                    UdiRowDetails(
                        title = "Prima mensual",
                        content = formatMoney(udiGlobalDetails.udiBonus[0].monthlyBonus)
                    )
                    UdiRowDetails(
                        title = "Recargo por cargo fraccionado",
                        content = formatMoney(udiGlobalDetails.udiBonus[0].udiCommission)
                    )
                    UdiRowDetails(
                        title = "Prima total",
                        content = formatMoney(udiGlobalDetails.udiBonus[0].monthlyTotalBonus)
                    )
                    UdiRowDetails(
                        title = "Prima anual total",
                        content = formatMoney(udiGlobalDetails.udiBonus[0].yearlyBonus)
                    )
                }
            }
        }
    }
}

@Composable
fun CardDetails(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding( top = 10.dp, bottom = 10.dp)
            .wrapContentHeight()
            .then(modifier)
    ) {
        content()
    }
}