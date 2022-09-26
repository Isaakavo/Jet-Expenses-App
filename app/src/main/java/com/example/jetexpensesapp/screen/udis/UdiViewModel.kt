package com.example.jetexpensesapp.screen.udis

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.data.DataOrException
import com.example.jetexpensesapp.data.UdiGlobalDetails
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.model.UdiItem
import com.example.jetexpensesapp.repository.UdiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    var globalValues by mutableStateOf<UdiGlobalDetails>(UdiGlobalDetails())

    init {
        getUdiForToday(LocalDateTime.now())
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUdis().distinctUntilChanged().collect { udis ->
                if (udis.isEmpty()) {
                    Log.d("Empty", "Empty list")
                } else {
                    val ordered = udis.sortedBy { it.dateOfPurchase }
                    _dataFromDb.value = ordered
                    withContext(Dispatchers.Main) {
                        if (globalValues.totalExpend == 0.0 && globalValues.udisTotal == 0.0 && globalValues.udisConvertion == 0.0)
                            getUdiGlobalDetails()
                    }
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
                udiFromApi = udiFromApi.copy(loading = false)
            }
        }
    }

    fun getUdiGlobalDetails() {
        _dataFromDb.value.map {
            globalValues.totalExpend += it.purchaseTotal
            globalValues.udisTotal += it.totalOfUdi
        }
        globalValues.udisConvertion =
            globalValues.udisTotal * (udiFromApi.data?.udiValue?.toDouble()
                ?: 0.0)
    }

    fun addUdi(retirementPlan: RetirementPlan) {
        viewModelScope.launch {
            repository.addUdi(retirementPlan)
        }
    }

}