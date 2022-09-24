package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.runtime.Composable
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

@Composable
fun GenericRow(
    retirementPlan: RetirementPlan? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = RectangleShape,
    elevation: Dp = 12.dp,
    icon: ImageVector = Icons.Filled.ShowChart,
    rightIcon: @Composable () -> Unit
) {
    Surface(
        modifier,
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
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon, contentDescription = null,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Cobro $${retirementPlan?.purchaseTotal}",
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                        )
                        Text(
                            text = "Udis ${retirementPlan?.udiValue}",
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .wrapContentHeight()
                        )
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    contentAlignment = Alignment.Center
                ) {
                    rightIcon()
                }
            }
        }
        Divider(
            startIndent = 8.dp,
            color = Color.LightGray,
            modifier = Modifier.fillMaxWidth().padding(bottom = 45.dp),
            thickness = 1.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GenericRowPreview() {
    GenericRow(
        retirementPlan = RetirementData().load()[0]
    ) {
        Icon(imageVector = Icons.Filled.ArrowRight, contentDescription = null)
    }
}