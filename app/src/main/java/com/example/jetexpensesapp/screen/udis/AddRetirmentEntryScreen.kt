package com.example.jetexpensesapp.screen.udis

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetexpensesapp.components.RetirementInputText
import com.example.jetexpensesapp.components.UdiEntryDetails
import com.example.jetexpensesapp.components.shared.TopBar
import com.example.jetexpensesapp.data.RetirementData
import java.util.*
//TODO Refactor all this
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AddRetirementEntryScreen(
    @StringRes topBarTitle: Int,
    onUdiUpdate: () -> Unit,
    onBack: () -> Unit,
    viewModel: AddEditUdiViewmodel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//    val amount = remember {
//        mutableStateOf("")
//    }
//
//    val udiCall = remember {
//        mutableStateOf(false)
//    }
//
//    val singleRetirementPlan =
//        viewModel.singleRetirementPlan.value
//
//
//    val date = remember {
//        mutableStateOf(formatDateForRequest(LocalDateTime.now()))
//    }
//
//    var dateSupp by remember {
//        mutableStateOf(formatDateForRequest(LocalDateTime.now()))
//    }

//    if (retirementPlanId != null && singleRetirementPlan == null) {
//        viewModel.getUdiById(retirementPlanId)
//    } else if (retirementPlanId != null && singleRetirementPlan != null && !udiCall.value) {
//        amount.value = singleRetirementPlan.purchaseTotal.toString()
//        date.value = formatDateForRequest(singleRetirementPlan.dateOfPurchase)
//        udiCall.value = true
//    }

    val context = LocalContext.current
//    val udiValue = viewModel.udiFromApi.data?.udiValue?.toDouble() ?: "0".toDouble()
//    val amountDouble = (amount.value.toDoubleOrNull() ?: "0".toDouble())
//    val totalOfUdi = amountDouble / udiValue
//    val udiCommission = totalOfUdi - Constants.MINE_UDI
//    val udiValueInMoney = Constants.MINE_UDI * udiValue
//    val udiValueInMoneyCommission = udiCommission * udiValue
//
//    val retirement = RetirementPlan(
//        dateOfPurchase = formatStringToDate(date.value).atStartOfDay(),
//        purchaseTotal = amountDouble,
//        udiValue = udiValue,
//        totalOfUdi = totalOfUdi,
//        mineUdi = Constants.MINE_UDI,
//        udiCommission = checkNegativeNumber(udiCommission),
//        udiValueInMoney = checkNegativeNumber(udiValueInMoney),
//        udiValueInMoneyCommission = checkNegativeNumber(udiValueInMoneyCommission)
//    )

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
            viewModel.updateDate("$year-${monthTemp}-$dayTemp")
            //viewModel.getUdiForToday(formatStringToDate(date.value).atStartOfDay())
        }, mYear, mMonth, mDay
    )
    //mDatePickerDialog.datePicker.maxDate = mDay.plus(5L)

//    if (viewModel.udiFromApi.loading == true) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            CircularProgressIndicator()
//        }
//        Log.d("Loading", "loading...")
//    } else if (viewModel.udiFromApi.e != null) {
//        Card() {
//            Text(text = "Something went wrong!!")
//        }
//    } else {
    Column() {
        TopBar(
            navControllerAction = {

                //navController.popBackStack()
            },
            buttonText = "add",
            backgroundColor = Color.Transparent,
            onClick = (viewModel::saveUdi)
        )
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
                    text = uiState.amount,
                    label = "Total comprado",
                    keyboardType = KeyboardType.Number,
                    onTextChange = viewModel::updateAmount
                )
                RetirementInputText(
                    text = uiState.date,
                    label = "Fecha del cargo",
                    modifier = Modifier
                        .padding(top = 6.dp),
                    onTextChange = (viewModel::updateDate),
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
            }
        }
        UdiEntryDetails(
            retirementPlan = RetirementData().load()[0],
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxHeight()
        )
    }
    //}

    LaunchedEffect(uiState.isUdiSaved) {
        if (uiState.isUdiSaved) {
            onUdiUpdate()
        }
    }
}