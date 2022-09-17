package com.example.jetexpensesapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetexpensesapp.components.RetirementButton
import com.example.jetexpensesapp.components.RetirementInputText
import com.example.jetexpensesapp.components.UdiRow
import com.example.jetexpensesapp.data.RetirementData
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.utils.DateTransformation
import com.example.jetexpensesapp.utils.formatDate
import java.time.LocalDateTime
import java.util.*

@Composable
fun AddRetirementEntry() {

    var amount by remember {
        mutableStateOf("0")
    }

    val date by remember {
        mutableStateOf(LocalDateTime.now())
    }

    val retirement by remember {
        mutableStateOf(
            RetirementPlan(
                dateOfPurchase = date,
                purchaseTotal = amount.toDouble(),
                udiValue = 7.50,
                totalOfUdi = (amount.toDouble() / 7.50),
                mineUdi = 437.12,
                udiCommission = (amount.toDouble() / 7.50) - 437.12,
                udiValueInMoney = 437.12 * 7.50,
                udiValueInMoneyCommission = ((amount.toDouble() / 7.50) - 437.12) * 7.50
            )
        )
    }

    Column {
        Column(Modifier.padding(60.dp)) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                RetirementInputText(
                    text = amount,
                    label = "Total comprado",
                    keyboardType = KeyboardType.Number,
                    onTextChange = {
                        if (it.all { char -> char.isDigit() }) {
                            amount = it
                            if (it.isNotEmpty() && it.toDouble() > 0) retirement.purchaseTotal =
                                it.toDouble()
                        }
                    })
                RetirementInputText(text = formatDate(date), label = "Fecha del cargo",
                    onTextChange = {
                        // if (it.length <= 10) date = it
                    })
            }
            Column(Modifier.padding(top = 16.dp)) {
                UdiRow(retirementPlan = retirement)
                RetirementButton(text = "Agregar", onClick = {
                    if (amount.isNotEmpty() && amount != "0" && date != null) {
                        // add to viewmodel
                    }
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRetirementEntryPreview() {
    AddRetirementEntry()
}