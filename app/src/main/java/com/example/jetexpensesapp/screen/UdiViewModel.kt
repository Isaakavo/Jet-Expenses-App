package com.example.jetexpensesapp.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.data.DataOrException
import com.example.jetexpensesapp.data.RetirementData
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.model.UdiItem
import com.example.jetexpensesapp.repository.UdiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UdiViewModel @Inject constructor(private val repository: UdiRepository) : ViewModel() {
//    var dataFromDb: MutableState<DataOrException<ArrayList<RetirementPlan>, Boolean, Exception>> =
//        mutableStateOf(DataOrException(null, true, Exception("")))

    private val _dataFromDb = MutableStateFlow<List<RetirementPlan>>(emptyList())
    val dataFromDb = _dataFromDb.asStateFlow()

    var udiFromApi:  MutableState<DataOrException<UdiItem, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        getUdiForToday()
        viewModelScope.launch(Dispatchers.IO) {
            _dataFromDb.value = RetirementData().load()
        }
    }

    private fun getUdiForToday() {
        viewModelScope.launch {
            udiFromApi.value.loading = true
            udiFromApi.value = repository.getUdiForToday()
            if (udiFromApi.value.data.toString().isNotEmpty()) {
                udiFromApi.value.loading = false
            }
        }
    }

    private fun getRetirementObjFromDb(){

    }
}