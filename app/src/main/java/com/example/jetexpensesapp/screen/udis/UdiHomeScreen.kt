package com.example.jetexpensesapp.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetexpensesapp.components.shared.*
import com.example.jetexpensesapp.data.UdiGlobalDetails
import com.example.jetexpensesapp.model.udi.Data
import com.example.jetexpensesapp.model.udi.RetirementRecord
import com.example.jetexpensesapp.screen.udis.UdiViewModel
import com.example.jetexpensesapp.screen.udis.UdisDateFilterType
import com.example.jetexpensesapp.utils.formatDateFromServer

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun UdiHomeScreen(
    @StringRes userMessage: Int,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onAddEntry: () -> Unit,
    onEditEntry: (RetirementRecord) -> Unit,
    onDeleteEntry: (RetirementRecord) -> Unit,
    onUdiClick: (Data) -> Unit,
    onDetailsClick: (UdiGlobalDetails) -> Unit,
    onUserMessageDisplayed: () -> Unit,
    viewModel: UdiViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                title = "Expenses App",
                titleWeight = FontWeight.Bold,
                icon = null,
                backgroundColor = MaterialTheme.colors.primary,
                shouldDisplayFilter = true,
                onFilterNew = { viewModel.setFiltering(UdisDateFilterType.NEW_TO_LAST) },
                onFilterLast = { viewModel.setFiltering(UdisDateFilterType.LAST_TO_NEW) }
            )
        }
    ) {
        Column {
            UdiValueDisplay(udiValueToday = uiState.udiValueToday)
            UdiGlobalDetail(
                udisGlobalDetails = uiState.globalTotals,
                onAddEntry = onAddEntry,
                onDetailsClick = { onDetailsClick(uiState.globalTotals) }
            )
            UdisContent(
                loading = uiState.isLoading,
                udis = uiState.udis,
                isError = uiState.isError,
                userMessage = uiState.userMessage,
                errorMessage = uiState.errorMessage,
                onRefresh = viewModel::getAllUdis,
                onUdiClick = onUdiClick,
                onEditEntry = onEditEntry,
                onDeleteEntry = onDeleteEntry,
                onAddEntry = onAddEntry
            )
        }
    }

    uiState.userMessage?.let { message ->
        val snackBarText = stringResource(id = message)
        LaunchedEffect(scaffoldState.snackbarHostState, viewModel, message, snackBarText) {
            scaffoldState.snackbarHostState.showSnackbar(snackBarText)
            viewModel.snackbarMessageShown()
        }
    }

    val currentOnUserMessageDisplayed by rememberUpdatedState(newValue = onUserMessageDisplayed)
    LaunchedEffect(scaffoldState.snackbarHostState, userMessage) {
        if (userMessage != 0) {
            viewModel.showEditResultMessage(userMessage)
            currentOnUserMessageDisplayed()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UdisContent(
    loading: Boolean,
    udis: List<Data>,
    isError: Boolean,
    userMessage: Int?,
    errorMessage: String,
    onUdiClick: (Data) -> Unit,
    onEditEntry: (RetirementRecord) -> Unit,
    onDeleteEntry: (RetirementRecord) -> Unit,
    onRefresh: () -> Unit,
    onAddEntry: () -> Unit,
    modifier: Modifier = Modifier
) {

    val screenPadding = Modifier.padding(
        horizontal = 16.dp,
        vertical = 16.dp,
    )
    val commonModifier = modifier
        .fillMaxWidth()
        .then(screenPadding)

    LoadingContent(
        loading = loading,
        empty = udis.isEmpty() && !loading,
        emptyContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (userMessage != null) {
                    Text(
                        text = stringResource(id = userMessage),
                        modifier = commonModifier,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


        },
        onRefresh = onRefresh ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.End
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
            ) {
                itemsIndexed(udis) { index, udiObj ->
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToEnd)
                                udiObj.retirementRecord?.let { it1 -> onEditEntry(it1) }
                            else if (it == DismissValue.DismissedToStart) {
                                udiObj.retirementRecord?.let { it1 -> onDeleteEntry(it1) }
                            }
                            it != DismissValue.DismissedToEnd
                        }
                    )
                    val formattedDate =
                        formatDateFromServer(udiObj.retirementRecord?.dateOfPurchase)
                    if (index != 0 && formatDateFromServer(udis[index - 1].retirementRecord?.dateOfPurchase) != formattedDate) {
                        DateRow(
                            date = formattedDate,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    } else if (index == 0) {
                        DateRow(
                            date = formattedDate,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                    SwipeToEditOrDelete(
                        dismissState = dismissState,
                        retirementRecord = udiObj,
                        onUdiClick = onUdiClick
                    )

                }
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToEditOrDelete(
    dismissState: DismissState,
    retirementRecord: Data,
    onUdiClick: (Data) -> Unit
) {
    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier.padding(vertical = 4.dp),
        directions = setOf(
            DismissDirection.StartToEnd,
            DismissDirection.EndToStart
        ),
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> Color.Red
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Done
                DismissDirection.EndToStart -> Icons.Default.Delete
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Localized description",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = {
            Column {
                GenericRow(
                    data = retirementRecord,
                    onUdiClick = onUdiClick
                )
            }
        }
    )
}