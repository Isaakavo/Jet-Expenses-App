package com.example.jetexpensesapp.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetexpensesapp.components.RetirementButton
import com.example.jetexpensesapp.components.RetirementInputText
import com.example.jetexpensesapp.components.UdiEntryDetails
import com.example.jetexpensesapp.components.UdiRow
import com.example.jetexpensesapp.data.RetirementData
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.repository.UdiRepository
import com.example.jetexpensesapp.utils.Constants
import com.example.jetexpensesapp.utils.checkNegativeNumber
import com.example.jetexpensesapp.utils.formatDateForRequest
import com.example.jetexpensesapp.utils.formatStringToDate
import java.time.LocalDateTime

@Composable
fun AddRetirementEntry(viewModel: UdiViewModel = hiltViewModel()) {

    var amount by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf(formatDateForRequest(LocalDateTime.now()))
    }

    var dateSupp by remember {
        mutableStateOf(formatDateForRequest(LocalDateTime.now()))
    }

    val context = LocalContext.current
    val udiValue = viewModel.udiFromApi.data?.udiValue?.toDouble() ?: "0".toDouble()
    val amountDouble = (amount.toDoubleOrNull() ?: "0".toDouble())
    val totalOfUdi = amountDouble / udiValue
    val udiCommission = totalOfUdi - Constants.MINE_UDI
    val udiValueInMoney = Constants.MINE_UDI * udiValue
    val udiValueInMoneyCommission = udiCommission * udiValue

    val retirement = RetirementPlan(
        dateOfPurchase = formatStringToDate(date).atStartOfDay(),
        purchaseTotal = amountDouble,
        udiValue = udiValue,
        totalOfUdi = totalOfUdi,
        mineUdi = Constants.MINE_UDI,
        udiCommission = checkNegativeNumber(udiCommission),
        udiValueInMoney = checkNegativeNumber(udiValueInMoney),
        udiValueInMoneyCommission = checkNegativeNumber(udiValueInMoneyCommission)
    )

    Column(Modifier.padding(40.dp)) {
        Surface(
            modifier = Modifier.padding(4.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = 5.dp
        ) {
            Column(
                Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RetirementInputText(
                    text = amount,
                    label = "Total comprado",
                    keyboardType = KeyboardType.Number,
                    onTextChange = {
                        amount = if (amount == "0") "" else it
                    })
                RetirementInputText(
                    text = dateSupp,
                    label = "Fecha del cargo",
                    modifier = Modifier.padding(top = 6.dp),
                    onTextChange = {
                        if (it.length == 10) {
                            date = it
                            dateSupp = it
                            viewModel.getUdiForToday(formatStringToDate(it).atStartOfDay())
                        } else {
                            if (it.length <= 10 && dateSupp.length <= 10) {
                                dateSupp = it
                            }
                        }
                    })
            }
        }
        Column(
            Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.padding(4.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = 5.dp
            ) {
                if (viewModel.udiFromApi.loading == true) {
                    CircularProgressIndicator()
                    Log.d("Loading", "loading...")
                } else {
                    UdiEntryDetails(
                        retirementPlan = retirement
                    )
                }
            }
            RetirementButton(text = "Agregar", onClick = {
                if (amount.isNotEmpty() && amount != "0") {
                    // add to viewmodel
                    viewModel.addUdi(retirement)
                    Toast.makeText(context, "UDI Added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Por favor, agregue un total", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRetirementEntryPreview() {
    AddRetirementEntry()
}