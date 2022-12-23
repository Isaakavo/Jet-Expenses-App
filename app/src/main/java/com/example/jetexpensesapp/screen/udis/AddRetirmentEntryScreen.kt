package com.example.jetexpensesapp.screen.udis

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetexpensesapp.components.UdiEntryDetails
import com.example.jetexpensesapp.components.shared.*
import java.time.LocalTime
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AddRetirementEntryScreen(
    @StringRes topBarTitle: Int,
    isInsertCommission: Boolean = false,
    onUdiUpdate: () -> Unit,
    onBack: () -> Unit = {},
    onDeleteUdi: () -> Unit = {},
    viewModel: AddEditUdiViewmodel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.updateIsCommission(isInsertCommission)

    AddEditContent(
        topBarTitle = topBarTitle,
        isInsertCommission = isInsertCommission,
        udiValue = uiState.udiValue,
        amount = uiState.amount,
        udiCommission = uiState.udiCommission,
        yearlyBonus = uiState.yearlyBonus,
        monthlyTotalBonus = uiState.monthlyTotalBonus,
        date = uiState.date,
        loading = uiState.isLoading,
        retirementData = uiState,
        shouldDisplayBottomSheet = uiState.shouldDisplayBottomSheet,
        onBack = onBack,
        onSave = viewModel::save,
        onAmountChange = viewModel::updateAmount,
        onCommissionAmountChange = viewModel::updateCommissionAmount,
        onYearlyCommissionChange = viewModel::updateYearlyBonusAmount,
        onDateChange = viewModel::updateDate,
        onHandleDateRequest = viewModel::handleDateRequest
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
    isInsertCommission: Boolean,
    udiValue: String,
    amount: String,
    udiCommission: String,
    yearlyBonus: String,
    monthlyTotalBonus: String,
    date: String,
    loading: Boolean,
    retirementData: AddEditUdiUiState?,
    shouldDisplayBottomSheet: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onAmountChange: (String) -> Unit,
    onCommissionAmountChange: (String) -> Unit,
    onYearlyCommissionChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onHandleDateRequest: (String) -> Unit
) {

    val showDatePicker = remember { mutableStateOf(false) }

    if (showDatePicker.value) {
        DatePicker(
            date = date,
            onDateSelected = { dateRequest ->
                showDatePicker.value = false
                val time = LocalTime.now()
                val dateToVM = "${dateRequest}T$time"
                onHandleDateRequest(dateToVM)
            }) {
            showDatePicker.value = false
        }
    }

    LoadingContent(
        loading = loading,
        empty = retirementData == null && !loading,
        emptyContent = { /*TODO*/ },
        onRefresh = { /*TODO*/ }) {
        Scaffold(topBar = {
            TopBar(
                onBack = onBack,
                buttonText = stringResource(id = topBarTitle),
                onClick = onSave,
                icon = if (isInsertCommission) null else Icons.Filled.ArrowBack
            )
        }) {
            Column(Modifier.fillMaxHeight()) {
                if (isInsertCommission) {
                    Row() {
                        Text(
                            text = "Agrega los cobros que vienen en tu prima dentro de tu contrato",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    UdiValueDisplay(udiValueToday = udiValue)
                }
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
                        val mutableInteractionSource = remember { MutableInteractionSource() }
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            // works like onClick
                                            showDatePicker.value = !showDatePicker.value
                                        }
                                    }
                                }
                            }
                        RetirementInputText(
                            text = amount,
                            label = if (!isInsertCommission) "Total comprado" else "Prima mensual",
                            keyboardType = KeyboardType.Number,
                            onTextChange = onAmountChange
                        )

                        RetirementInputText(
                            text = if (!isInsertCommission) date else udiCommission,
                            label = if (!isInsertCommission) "Fecha del cargo" else "Recargo por cargo fraccionado",
                            readOnly = !isInsertCommission,
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier
                                .padding(top = 6.dp),
                            onTextChange = if (!isInsertCommission) onDateChange else onCommissionAmountChange,
                            interactionSource = if (!isInsertCommission) mutableInteractionSource else MutableInteractionSource()
                        )

                        if (isInsertCommission) {
                            RetirementInputText(
                                text = yearlyBonus,
                                label = "Prima anual total",
                                readOnly = true,
                                onTextChange = onYearlyCommissionChange
                            )

                            RetirementInputText(
                                text = monthlyTotalBonus,
                                label = "Prima mensual total",
                                readOnly = true,
                                onTextChange = {}
                            )
                        }
                    }
                }

                if (shouldDisplayBottomSheet) {
                    retirementData?.dataObj?.let {
                        UdiEntryDetails(
                            data = it,
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }

    }

}