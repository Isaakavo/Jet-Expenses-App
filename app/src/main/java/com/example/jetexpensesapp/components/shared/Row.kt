package com.example.jetexpensesapp.components.shared

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jetexpensesapp.data.RetirementData
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.utils.formatDate
import com.example.jetexpensesapp.utils.formatNumber
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenericRow(
    retirementPlan: RetirementPlan? = null,
    retirementData: MutableState<RetirementPlan>? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = RectangleShape,
    elevation: Dp = 6.dp,
    sheetState: ModalBottomSheetState? = null,
    showModalSheet: MutableState<Boolean> = mutableStateOf(false)
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier.fillMaxHeight().clickable {
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
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = modifier
                    .padding(5.dp)
            ) {
                Text(
                    text = formatDate(retirementPlan!!.dateOfPurchase),
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Cobro $${retirementPlan?.purchaseTotal}",
                            Modifier
                                .fillMaxWidth(0.3f)
                        )
                        Text(
                            text = "Valor UDI ${retirementPlan?.udiValue}",
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                        )
                        Text(
                            text = "Total UDIS ${formatNumber(retirementPlan?.totalOfUdi)}",
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                        )
                    }
                }
            }
        }
    }
//    Divider(
//        color = Color.LightGray,
//        startIndent = 16.dp,
//        modifier = Modifier.padding(end = 16.dp)
//    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun GenericRowPreview() {
    GenericRow(
        retirementPlan = RetirementData().load()[0]
    )
}