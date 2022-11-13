package com.example.jetexpensesapp.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetexpensesapp.model.udi.RetirementPlan
import com.example.jetexpensesapp.utils.formatDateFullMonth
import com.example.jetexpensesapp.utils.formatMoney

@Composable
fun UdiEntryDetails(
    retirementPlan: RetirementPlan,
    modifier: Modifier = Modifier,
    editOptions: @Composable () -> Unit = {}
) {
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
                    text = formatDateFullMonth(retirementPlan.dateOfPurchase),
                    textAlign = TextAlign.Center,
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
                title = "Total Comprado",
                content = formatMoney(retirementPlan.purchaseTotal)
            )
            UdiRowDetails(
                title = "Valor de la UDI",
                content = "$" + retirementPlan.udiValue
            )
            UdiRowDetails(
                title = "Total de UDIS",
                content = String.format("%.2f", retirementPlan.totalOfUdi)
            )
            UdiRowDetails(
                title = "Mis UDIS",
                content = String.format("%.2f", retirementPlan.mineUdi)
            )
            UdiRowDetails(
                title = "Comision en UDIS",
                content = String.format("%.2f", retirementPlan.udiCommission)
            )
            UdiRowDetails(
                title = "Conversion",
                content = formatMoney(retirementPlan.udiValueInMoney)
            )
            UdiRowDetails(
                title = "Conversion de la comision",
                content = formatMoney(retirementPlan.udiValueInMoneyCommission)
            )
            editOptions()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun previewList() {
//    val udisObj = RetirementData().load()
//    UdiEntryDetails(retirementPlan = udisObj[0]) {}
}