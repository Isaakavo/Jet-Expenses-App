package com.example.jetexpensesapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetexpensesapp.data.RetirementData
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.utils.formatDate

@Composable
fun UdiEntryDetails(
    retirementPlan: RetirementPlan
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        UdiRowAddEntry(
            title = "Total Comprado",
            content = "$" + String.format("%.2f", retirementPlan.purchaseTotal)
        )
        UdiRowAddEntry(
            title = "Valor de la UDI",
            content = "$" + retirementPlan.udiValue
        )
        UdiRowAddEntry(
            title = "Total de UDIS",
            content = String.format("%.2f", retirementPlan.totalOfUdi)
        )
        UdiRowAddEntry(
            title = "Mis UDIS",
            content = String.format("%.2f", retirementPlan.mineUdi)
        )
        UdiRowAddEntry(
            title = "Comision en UDIS",
            content = String.format("%.2f", retirementPlan.udiCommission)
        )
        UdiRowAddEntry(
            title = "Conversion",
            content = "$" + String.format("%.2f", retirementPlan.udiValueInMoney)
        )
        UdiRowAddEntry(
            title = "Conversion de la comision",
            content = "$" + String.format("%.2f", retirementPlan.udiValueInMoneyCommission)
        )
        UdiRowAddEntry(
            title = "Fecha de Compra",
            content = formatDate(retirementPlan.dateOfPurchase)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun previewList() {
    val udisObj = RetirementData().load()
    UdiEntryDetails(retirementPlan = udisObj[0])
}