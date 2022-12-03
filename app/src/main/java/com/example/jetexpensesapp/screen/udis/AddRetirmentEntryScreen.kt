package com.example.jetexpensesapp.screen.udis

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
import com.example.jetexpensesapp.components.shared.DatePicker
import com.example.jetexpensesapp.components.shared.LoadingContent
import com.example.jetexpensesapp.components.shared.TopBar
import com.example.jetexpensesapp.utils.toLocalDateTime

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AddRetirementEntryScreen(
    @StringRes topBarTitle: Int,
    isInsertCommission: Boolean = false,
    onUdiUpdate: () -> Unit,
    onBack: () -> Unit,
    onDeleteUdi: () -> Unit,
    viewModel: AddEditUdiViewmodel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val datePicker = DatePicker.getDatePicker(
        context,
        onRequest = {
            viewModel.updateDate(it)
            viewModel.getUdiForToday(it.toLocalDateTime())
        })

    viewModel.updateIsCommission(isInsertCommission)

    AddEditContent(
        topBarTitle = topBarTitle,
        isInsertCommission = isInsertCommission,
        amount = uiState.amount,
        udiCommission = uiState.udiCommission,
        date = uiState.date,
        loading = uiState.isLoading,
        retirementData = uiState,
        shouldDisplayBottomSheet = uiState.shouldDisplayBottomSheet,
        onBack = onBack,
        onSave = viewModel::save,
        onAmountChange = viewModel::updateAmount,
        onCommissionAmountChange= viewModel::updateCommissionAmount,
        onDateChange = viewModel::updateDate,
        onShowDatePicker = datePicker::show
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
    amount: String,
    udiCommission: String,
    date: String,
    loading: Boolean,
    retirementData: AddEditUdiUiState?,
    shouldDisplayBottomSheet: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onAmountChange: (String) -> Unit,
    onCommissionAmountChange: (String) -> Unit,
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
                onClick = (onSave)
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
                        label = if (isInsertCommission) "Total comprado" else "Total de udis tuyas",
                        keyboardType = KeyboardType.Number,
                        onTextChange = onAmountChange
                    )
                    if (isInsertCommission) {
                        RetirementInputText(
                            text = udiCommission,
                            label = "Comision",
                            keyboardType = KeyboardType.Number,
                            onTextChange = onCommissionAmountChange
                        )
                    }
                    if (!isInsertCommission) {
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