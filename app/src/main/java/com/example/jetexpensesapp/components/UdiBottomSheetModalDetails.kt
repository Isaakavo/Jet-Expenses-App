package com.example.jetexpensesapp.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetexpensesapp.components.shared.GenericRow
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.screen.udis.UdiViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UdiBottomSheetModalDetails(
    viewModel: UdiViewModel
) {
    val udisObj = viewModel.dataFromDb.collectAsState().value
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

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
                        onClick = { /*TODO*/ }) {
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
        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {
            items(udisObj) { udiObj ->
                GenericRow(
                    retirementPlan = udiObj,
                    retirementData = retirementData,
                    sheetState = bottomSheetState,
                    showModalSheet = showModalSheet,
                    modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp),
                    elevation = 6.dp,
                    shape = RoundedCornerShape(15.dp)
                )
            }
        }

    }
}