package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jetexpensesapp.model.udi.RetirementPlan

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenericRow(
    retirementPlan: RetirementPlan? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp,
    onUdiClick: (RetirementPlan) -> Unit
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier
            .clickable {
                if (retirementPlan != null) {
                    onUdiClick(retirementPlan)
                }
            },
        shape,
        elevation = elevation
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                Icons.Filled.CurrencyExchange,
                contentDescription = null,
                modifier = Modifier
                    .weight(0.25f)
                    .align(alignment = CenterVertically)
            )
            Text(
                text = "Cobro $${retirementPlan?.purchaseTotal}",
                Modifier
                    .weight(0.25f)
                    .align(alignment = CenterVertically),
                textAlign = TextAlign.Left
            )
            Text(
                text = "Valor UDI ${retirementPlan?.udiValue}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.25f)
                    .align(alignment = CenterVertically)
            )
            Icon(
                Icons.Filled.ArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .weight(0.25f)
                    .align(alignment = CenterVertically)
            )
        }
    }
}