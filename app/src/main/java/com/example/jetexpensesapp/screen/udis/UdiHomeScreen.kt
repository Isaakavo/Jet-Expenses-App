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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetexpensesapp.components.shared.*
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.screen.udis.UdiViewModel
import com.example.jetexpensesapp.screen.udis.UdisDateFilterType
import com.example.jetexpensesapp.utils.formatDate

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun UdiHomeScreen(
    @StringRes userMessage: Int,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onAddEntry: () -> Unit,
    onEditEntry: (RetirementPlan) -> Unit,
    onDeleteEntry: (RetirementPlan) -> Unit,
    onUdiClick: (RetirementPlan) -> Unit,
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
            GlobalDetail(udisGlobalDetails = uiState.globalTotals, onAddEntry = onAddEntry)
            UdisContent(
                loading = uiState.isLoading,
                udis = uiState.udis,
                onUdiClick = onUdiClick,
                onEditEntry = onEditEntry,
                onDeleteEntry = onDeleteEntry,
                onAddEntry = onAddEntry
            )
        }
    }

    uiState.userMessage?.let { message ->
        val snackBarText = stringResource(id = message)
        LaunchedEffect(scaffoldState, viewModel, message, snackBarText) {
            scaffoldState.snackbarHostState.showSnackbar(snackBarText)
            viewModel.snackbarMessageShown()
        }
    }

    val currentOnUserMessageDisplayed by rememberUpdatedState(newValue = onUserMessageDisplayed)
    LaunchedEffect(userMessage) {
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
    udis: List<RetirementPlan>,
    onUdiClick: (RetirementPlan) -> Unit,
    onEditEntry: (RetirementPlan) -> Unit,
    onDeleteEntry: (RetirementPlan) -> Unit,
    onAddEntry: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = loading,
        empty = udis.isEmpty() && !loading,
        emptyContent = { /*TODO*/ },
        onRefresh = { /*TODO*/ }) {
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
                                onEditEntry(udiObj)
                            else if (it == DismissValue.DismissedToStart) {
                                onDeleteEntry(udiObj)
                            }
                            it != DismissValue.DismissedToEnd
                        }
                    )
                    val formattedDate = formatDate(udiObj.dateOfPurchase)
                    if (index != 0 && formatDate(udis[index - 1].dateOfPurchase) != formattedDate) {
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
                    //TODO Extract this to another file
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
                                    retirementPlan = udiObj,
                                    onUdiClick = onUdiClick
                                )
                            }
                        }
                    )
                }
            }

        }
    }
}