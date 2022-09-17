package com.example.jetexpensesapp.screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class UdiViewModel @Inject constructor(private val repository: UdiRepository) : ViewModel() {

    private val _dataFromDb = MutableStateFlow<List<RetirementPlan>>(emptyList())
    val dataFromDb = _dataFromDb.asStateFlow()

    var udiFromApi by mutableStateOf<DataOrException<UdiItem, Boolean, Exception>>(
        DataOrException(
            null,
            false,
            Exception("")
        )
    )

    init {
        getUdiForToday(LocalDateTime.now())
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUdis().distinctUntilChanged().collect { udis ->
                if (udis.isEmpty()) {
                    Log.d("Empty", "Empty list")
                } else {
                    _dataFromDb.value = udis
                }
            }
        }
    }

    fun getUdiForToday(date: LocalDateTime) {
        viewModelScope.launch {
            Log.d("viewmodel", "Calling api")
            udiFromApi = udiFromApi.copy(loading = true)
            udiFromApi = repository.getUdiForToday(date)

            if (udiFromApi.data.toString().isNotEmpty()) {
                Log.d("viewmodel", "Result from api: $udiFromApi")
                //udiFromApi.loading = false
                udiFromApi = udiFromApi.copy(loading = false)
            }
        }
    }

    fun addUdi(retirementPlan: RetirementPlan) =
        viewModelScope.launch { repository.addUdi(retirementPlan) }
}