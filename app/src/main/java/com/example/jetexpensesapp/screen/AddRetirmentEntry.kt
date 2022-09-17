package com.example.jetexpensesapp.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetexpensesapp.components.RetirementButton
import com.example.jetexpensesapp.components.RetirementInputText
import com.example.jetexpensesapp.components.UdiRow
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.utils.formatDateForRequest
import com.example.jetexpensesapp.utils.formatStringToDate
import java.time.LocalDateTime

@Composable
fun AddRetirementEntry(viewModel: UdiViewModel = hiltViewModel()) {

    val datos = viewModel.udiFromApi.data?.data?.toDouble() ?: "0".toDouble()

    var amount by remember {
        mutableStateOf("0")
    }

    var date by remember {
        mutableStateOf(formatDateForRequest(LocalDateTime.now()))
    }

    var dateSupp by remember {
        mutableStateOf("")
    }

    val amountDouble = (amount.toDoubleOrNull() ?: "0".toDouble())

    val retirement = RetirementPlan(
        dateOfPurchase = formatStringToDate(date).atStartOfDay(),
        purchaseTotal = amountDouble,
        udiValue = datos,
        totalOfUdi = amountDouble / datos,
        mineUdi = 437.12,
        udiCommission = (amountDouble / datos) - 437.12,
        udiValueInMoney = 437.12 * datos,
        udiValueInMoneyCommission = ((amountDouble / 7.50) - 437.12) * 7.50
    )

    Column {
        Column(Modifier.padding(60.dp)) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                RetirementInputText(
                    text = amount,
                    label = "Total comprado",
                    keyboardType = KeyboardType.Number,
                    onTextChange = {
                        amount = it
                    })
                RetirementInputText(text = dateSupp, label = "Fecha del cargo",
                    onTextChange = {
                        if (it.length == 10) {
                            date = it
                            dateSupp = it
                            viewModel.getUdiForToday(formatStringToDate(it).atStartOfDay())
                        } else {
                            dateSupp = it
                        }
                    })
            }
            Column(
                Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.udiFromApi.loading == true) {
                    CircularProgressIndicator()
                    Log.d("Loading", "loading...")
                } else {
                    UdiRow(
                        retirementPlan = retirement
                    )
                    RetirementButton(text = "Agregar", onClick = {
                        if (amount.isNotEmpty() && amount != "0") {
                            // add to viewmodel
                        }
                    })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRetirementEntryPreview() {
    AddRetirementEntry()
}