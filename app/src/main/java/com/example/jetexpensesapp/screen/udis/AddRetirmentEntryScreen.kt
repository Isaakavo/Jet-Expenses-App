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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetexpensesapp.components.RetirementInputText
import com.example.jetexpensesapp.components.UdiEntryDetails
import com.example.jetexpensesapp.components.shared.LoadingContent
import com.example.jetexpensesapp.components.shared.TopBar
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.utils.formatStringToDate
import java.util.*

//TODO Refactor all this
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AddRetirementEntryScreen(
    @StringRes topBarTitle: Int,
    onUdiUpdate: () -> Unit,
    onBack: () -> Unit,
    onDeleteUdi: () -> Unit,
    viewModel: AddEditUdiViewmodel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val datePickerDialog = DatePickerDialog(
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
            val requestDate = "$year-${monthTemp}-$dayTemp"
            viewModel.updateDate(requestDate)
            viewModel.getUdiForToday(formatStringToDate(requestDate).atStartOfDay())
        }, mYear, mMonth, mDay
    )

    AddEditContent(
        topBarTitle = topBarTitle,
        amount = uiState.amount,
        date = uiState.date,
        loading = uiState.isLoading,
        retirementData = uiState,
        onBack = onBack,
        onSaveUdi = viewModel::saveUdi,
        onAmountChange = viewModel::updateAmount,
        onDateChange = viewModel::updateDate,
        onShowDatePicker = datePickerDialog::show
    )

    LaunchedEffect(uiState.isUdiSaved) {
        if (uiState.isUdiSaved) {
            onUdiUpdate()
        }
    }

    LaunchedEffect(uiState.isUdiDeleted) {
        if (uiState.isUdiDeleted) {
            onDeleteUdi()
        }
    }
}

@Composable
fun AddEditContent(
    @StringRes topBarTitle: Int,
    amount: String,
    date: String,
    loading: Boolean,
    retirementData: AddEditUdiUiState?,
    onBack: () -> Unit,
    onSaveUdi: () -> Unit,
    onAmountChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onShowDatePicker: () -> Unit
) {
    LoadingContent(
        loading = loading,
        empty = retirementData == null && !loading,
        emptyContent = { /*TODO*/ },
        onRefresh = { /*TODO*/ }) {
        Column() {
            TopBar(
                onBack = onBack,
                buttonText = stringResource(id = topBarTitle),
                backgroundColor = Color.Transparent,
                onClick = (onSaveUdi)
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
                        text = amount,
                        label = "Total comprado",
                        keyboardType = KeyboardType.Number,
                        onTextChange = onAmountChange
                    )
                    RetirementInputText(
                        text = date,
                        label = "Fecha del cargo",
                        modifier = Modifier
                            .padding(top = 6.dp),
                        onTextChange = onDateChange,
                        interactionSource = remember { MutableInteractionSource() }
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            // works like onClick
                                            onShowDatePicker()
                                        }
                                    }
                                }
                            }
                    )
                }
            }
            UdiEntryDetails(
                retirementPlan = RetirementPlan(
                    dateOfPurchase = formatStringToDate(retirementData?.date!!).atStartOfDay(),
                    purchaseTotal = if (retirementData.amount.isNotEmpty()) {
                        amount.toDouble()
                    } else {
                           0.0
                    },
                    udiValue = retirementData.udiValue.toDouble(),
                    udiValueInMoney = retirementData.udiValueInMoney,
                    udiValueInMoneyCommission = retirementData.udiValueInMoneyCommission,
                    totalOfUdi = retirementData.totalOfUdi,
                    udiCommission = retirementData.udiComission,
                    mineUdi = retirementData.mineUdi
                ),
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxHeight()
            )
        }
    }
}