package com.example.jetexpensesapp.screen.udis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetexpensesapp.components.UdiRowDetails
import com.example.jetexpensesapp.components.shared.TopBar

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

    TopBar(
        icon = Icons.Filled.ArrowBack,
        backgroundColor = Color.Transparent,
        navController = navController
    ) {
        navController?.popBackStack()
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        elevation = 10.dp
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Detalles", textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
            Divider(
                modifier = Modifier.padding(5.dp, end = 10.dp),
                color = Color.Black,
                startIndent = 10.dp
            )
            UdiRowDetails(
                title = "Total Gastado",
                content = "$" + String.format("%.2f", udisGlobalDetails.totalExpend)
            )
            UdiRowDetails(
                title = "Valor de la UDI",
                content = "$$udiForToday"
            )
            UdiRowDetails(
                title = "Total de UDIS",
                content = String.format("%.2f", udisGlobalDetails.udisTotal)
            )
            UdiRowDetails(
                title = "Conversion",
                content = String.format("%.2f", udisGlobalDetails.udisConvertion)
            )
        }
    }
}