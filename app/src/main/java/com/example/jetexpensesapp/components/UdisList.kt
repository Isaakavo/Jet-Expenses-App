package com.example.jetexpensesapp.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetexpensesapp.components.shared.GenericRow
import com.example.jetexpensesapp.data.RetirementData
import com.example.jetexpensesapp.screen.udis.UdiViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UdisList(viewModel: UdiViewModel) {
    val udisObj = viewModel.dataFromDb.collectAsState().value
    //val udisObj = RetirementData().load()
    //UdiBottomSheetModalDetails(retirementPlan = udisObj)
//    LazyColumn {
//        items(udisObj) { udiObj ->
//            //UdiBottomSheetModalDetails(retirementPlan = udiObj)
//            GenericRow(
//                udiObj,
//                modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp),
//                elevation = 6.dp,
//                shape = RoundedCornerShape(15.dp)
////            )
//        }
//    }
}

@Preview(showBackground = true)
@Composable
fun UdiListPreview() {
    // UdisList()
}