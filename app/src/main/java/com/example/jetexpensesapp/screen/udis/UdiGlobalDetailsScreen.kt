package com.example.jetexpensesapp.screen.udis

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetexpensesapp.components.UdiRowDetails
import com.example.jetexpensesapp.components.shared.TopBar
import com.example.jetexpensesapp.utils.formatMoney

@Composable
fun UdiGlobalDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: UdiViewModel = hiltViewModel(),
    navController: NavController? = null,
) {

    val udiForToday = viewModel.udiFromApi.data?.udiValue?.toDouble()
    val udisGlobalDetails = viewModel.globalValues

    if (udiForToday != null) {
        udisGlobalDetails.udisConvertion = udiForToday * udisGlobalDetails.udisTotal
    }
    Surface(
        modifier = modifier,
        elevation = 0.dp
    ) {
        Column {
            TopBar(
                backgroundColor = Color.Transparent,
                title = "Detalles Globales",
                navControllerAction = { navController?.popBackStack() }
            ) {
                navController?.popBackStack()
            }
            UdiRowDetails(
                title = "Total Gastado",
                content = formatMoney(udisGlobalDetails.totalExpend)
            )
            UdiRowDetails(
                title = "Valor de la UDI",
                content = udiForToday.toString()
            )
            UdiRowDetails(
                title = "Total de UDIS",
                content = String.format("%.2f", udisGlobalDetails.udisTotal)
            )
            UdiRowDetails(
                title = "Conversion",
                content = formatMoney(udisGlobalDetails.udisConvertion)
            )
            UdiRowDetails(
                title = "Rendimiento",
                content = formatMoney(
                    udisGlobalDetails.udisConvertion - udisGlobalDetails.totalExpend
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
        }
    }
}