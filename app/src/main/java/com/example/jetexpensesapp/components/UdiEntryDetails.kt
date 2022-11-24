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
import com.example.jetexpensesapp.model.udi.Data
import com.example.jetexpensesapp.utils.formatDateFromServer
import com.example.jetexpensesapp.utils.formatMoney

@Composable
fun UdiEntryDetails(
    data: Data,
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
                    text = formatDateFromServer(data.retirementRecord?.dateOfPurchase),
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
                content = formatMoney(data.retirementRecord?.purchaseTotal)
            )
            UdiRowDetails(
                title = "Valor de la UDI",
                content = "$" + data.retirementRecord?.udiValue
            )
            UdiRowDetails(
                title = "Total de UDIS",
                content = String.format(
                    "%.2f",
                    data.retirementRecord?.totalOfUdi ?: 0.0
                )
            )
            UdiRowDetails(
                title = "Mis UDIS",
                content = String.format("%.2f", data.retirementRecord?.udiCommission?.udiCommssion)
            )
            UdiRowDetails(
                title = "Comision en UDIS",
                content = String.format("%.2f", data.retirementRecord?.udiCommission?.userUdis)
            )
            UdiRowDetails(
                title = "Conversion",
                content = formatMoney(data.udiConversions?.udiConversion)
            )
            UdiRowDetails(
                title = "Conversion de la comision",
                content = formatMoney(data.udiConversions?.udiCommissionConversion)
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