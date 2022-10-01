package com.example.jetexpensesapp.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.components.shared.*
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.navigation.Screen
import com.example.jetexpensesapp.screen.udis.UdiViewModel
import com.example.jetexpensesapp.utils.formatDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UdiHomeScreen(
    navController: NavController,
    viewModel: UdiViewModel
) {
    val udisObj = viewModel.dataFromDb.collectAsState().value
    val udiGlobalDetails = viewModel.globalValues
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }

    val retirementData = remember {
        mutableStateOf(RetirementPlan())
    }

    fun hideSheet() {
        if (bottomSheetState.isVisible) {
            scope.launch {
                bottomSheetState.hide()
            }
            showModalSheet.value = false
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            UdiEntryDetails(retirementPlan = retirementData.value, modifier = Modifier) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(text = "Borrar",
                        modifier = Modifier.weight(0.5f),
                        shape = RectangleShape,
                        contentColor = colorResource(R.color.error),
                        icon = Icons.Filled.Delete,
                        variant = ButtonVariants.TEXT,
                        onClick = {
                            hideSheet()
                            viewModel.deleteUdi(retirementPlan = retirementData.value)
                        })
                    Button(text = "Editar",
                        modifier = Modifier.weight(0.5f),
                        shape = RectangleShape,
                        contentColor = colorResource(R.color.accepted),
                        icon = Icons.Filled.Edit,
                        variant = ButtonVariants.TEXT,
                        onClick = {
                            hideSheet()
                            navController.navigate(
                                Screen.AddRetirementEntryScreen.route + "/${retirementData.value.id}"
                            )
                        })
                }
            }
        },
        sheetElevation = 10.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.End
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
            ) {
                item {
                    TopBar(
                        title = "Expenses App",
                        titleWeight = FontWeight.Bold,
                        icon = null,
                        backgroundColor = MaterialTheme.colors.primary,
                    ) {

                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Udi hoy: $${viewModel.udiFromApi.data?.udiValue}",
                            modifier = Modifier.padding(top = 5.dp, end = 7.dp)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 10.dp)
                    ) {
                        GlobalDetail(udiGlobalDetails, navController)
                    }
                }
                itemsIndexed(udisObj) { index, udiObj ->
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToEnd) navController.navigate(
                                Screen.AddRetirementEntryScreen.route + "/${udiObj.id}"
                            ) else if (it == DismissValue.DismissedToStart) {
                                viewModel.deleteUdi(retirementPlan = udiObj)
                            }
                            it != DismissValue.DismissedToEnd
                        }
                    )
                    val formattedDate = formatDate(udiObj.dateOfPurchase)
                    if (index != 0 && formatDate(udisObj[index - 1].dateOfPurchase) != formattedDate) {
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
                                    retirementData = retirementData,
                                    sheetState = bottomSheetState,
                                    showModalSheet = showModalSheet
                                )
                            }
                        }
                    )
                }
            }

        }
    }
}