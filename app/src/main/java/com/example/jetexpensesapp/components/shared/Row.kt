package com.example.jetexpensesapp.components.shared

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jetexpensesapp.data.RetirementData
import com.example.jetexpensesapp.model.RetirementPlan
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenericRow(
    retirementPlan: RetirementPlan? = null,
    retirementData: MutableState<RetirementPlan>? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp,
    sheetState: ModalBottomSheetState? = null,
    showModalSheet: MutableState<Boolean> = mutableStateOf(false)
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier
            .fillMaxWidth()
            .clickable {
                showModalSheet.value = !showModalSheet.value
                scope.launch {
                    if (retirementPlan != null) {
                        retirementData?.value = retirementPlan
                    }
                    sheetState?.show()
                    Log.d("ROW", "Click, ${showModalSheet.value}, ${sheetState?.currentValue}")
                }
            },
        shape,
        elevation = elevation
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
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

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun GenericRowPreview() {
    GenericRow(
        retirementPlan = RetirementData().load()[0]
    )
}