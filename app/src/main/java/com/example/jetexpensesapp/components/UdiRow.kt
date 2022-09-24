package com.example.jetexpensesapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.utils.formatDate

@Composable
fun UdiRow(
    retirementPlan: RetirementPlan
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth(),
        color = Color(0xFFDFE6EB),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Total comprado:  ${String.format("%.2f", retirementPlan.purchaseTotal)}",
                style = MaterialTheme.typography.body1,
                fontSize = 18.sp
            )
            Text(
                text = "Valor de la udi: ${retirementPlan.udiValue}",
                style = MaterialTheme.typography.body1,
                fontSize = 18.sp
            )
            Text(
                text = "Total de udis: ${String.format("%.2f", retirementPlan.totalOfUdi)}",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "Mis Udis: ${retirementPlan.mineUdi}",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "Comision en udis: ${String.format("%.2f", retirementPlan.udiCommission)}",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "Conversion: ${String.format("%.2f", retirementPlan.udiValueInMoney)}",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "Conversion comision: ${String.format("%.2f", retirementPlan.udiValueInMoneyCommission)}",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "Fecha de compra: ${formatDate(retirementPlan.dateOfPurchase)}",
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}