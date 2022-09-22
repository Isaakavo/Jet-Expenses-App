package com.example.jetexpensesapp.screen.udis

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.utils.Constants
import com.example.jetexpensesapp.utils.checkNegativeNumber
import com.example.jetexpensesapp.utils.formatDateForRequest
import com.example.jetexpensesapp.utils.formatStringToDate
import java.time.LocalDateTime
import java.util.*

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

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, mDayOfMonth: Int ->
            var monthTemp = "${month + 1}"
            var dayTemp = "$mDayOfMonth"
            if (!monthTemp.matches(Regex("(0[1-9]|[12][0-9]|3[01])"))) {
                monthTemp = "0${month + 1}"
            }
            if (!dayTemp.matches(Regex("(0[1-9]|[12][0-9]|3[01])"))) {
                dayTemp = "0$dayTemp"
            }
            date = "$year-${monthTemp}-$dayTemp"
            viewModel.getUdiForToday(formatStringToDate(date).atStartOfDay())
        }, mYear, mMonth, mDay
    )
    //mDatePickerDialog.datePicker.maxDate = mDay.plus(5L)

    if (viewModel.udiFromApi.loading == true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        Log.d("Loading", "loading...")
    } else if (viewModel.udiFromApi.e != null) {
        Card() {
            Text(text = "Something went wrong!!")
        }
    } else {
        Surface(
            modifier = Modifier.padding(top = 15.dp, start = 25.dp, end = 25.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = 5.dp
        ) {
            Column(
                Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                RetirementInputText(
                    text = amount,
                    label = "Total comprado",
                    keyboardType = KeyboardType.Number,
                    onTextChange = {
                        amount = if (amount == "0") "" else it
                    })
                RetirementInputText(
                    text = date,
                    label = "Fecha del cargo",
                    modifier = Modifier
                        .padding(top = 6.dp),
                    onTextChange = {
                        if (it.length == 10) {
                            date = it
                            dateSupp = it
                            viewModel.getUdiForToday(formatStringToDate(it).atStartOfDay())
                        } else {
                            if (it.length <= 10 && dateSupp.length <= 10) {
                                date = it
                            }
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        // works like onClick
                                        mDatePickerDialog.show()
                                    }
                                }
                            }
                        }
                )

                UdiEntryDetails(
                    retirementPlan = retirement
                )

                RetirementButton(text = "Agregar", onClick = {
                    if (amount.isNotEmpty() && amount != "0") {
                        // add to viewmodel
                        viewModel.addUdi(retirement)
                        Toast.makeText(context, "UDI Added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor, agregue un total",
                            Toast.LENGTH_LONG
                        ).show()
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