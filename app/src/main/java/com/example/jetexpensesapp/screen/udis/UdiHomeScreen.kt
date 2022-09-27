package com.example.jetexpensesapp.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetexpensesapp.components.shared.DateRow
import com.example.jetexpensesapp.components.shared.GenericRow
import com.example.jetexpensesapp.components.shared.GlobalDetail
import com.example.jetexpensesapp.model.RetirementPlan
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

    Log.d("Modal", "value of remember ${showModalSheet}")

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            UdiEntryDetails(retirementPlan = retirementData.value, modifier = Modifier) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(modifier = Modifier.weight(0.5f),
                        onClick = {
                            viewModel.deleteUdi(retirementPlan = retirementData.value)
                            scope.launch {
                                bottomSheetState.hide()
                            }
                        }) {
                        Text(text = "Borrar")
                    }
                    Button(modifier = Modifier.weight(0.5f),
                        onClick = { /*TODO*/ }) {
                        Text(text = "Editar")
                    }
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
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 10.dp)
            ) {
                GlobalDetail(udiGlobalDetails, navController)
            }

            Card(
                modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                elevation = 5.dp
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                ) {
                    itemsIndexed(udisObj) { index, udiObj ->
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

                        GenericRow(
                            retirementPlan = udiObj,
                            retirementData = retirementData,
                            sheetState = bottomSheetState,
                            showModalSheet = showModalSheet,
                            modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp)
                        )
                    }
                }
            }
        }
    }
}