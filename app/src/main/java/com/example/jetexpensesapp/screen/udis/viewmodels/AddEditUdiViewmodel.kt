package com.example.jetexpensesapp.screen.udis

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.model.UdiItem
import com.example.jetexpensesapp.navigation.UdiDestinationArgs
import com.example.jetexpensesapp.repository.UdiRepository
import com.example.jetexpensesapp.utils.Constants
import com.example.jetexpensesapp.utils.checkNegativeNumber
import com.example.jetexpensesapp.utils.formatDateForRequest
import com.example.jetexpensesapp.utils.formatStringToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class AddEditUdiUiState(
    val amount: String = "0.0",
    val date: String = formatDateForRequest(LocalDateTime.now()),
    val udiValue: String = "0.0",
    val totalOfUdi: Double = 0.0,
    val mineUdi: Double = Constants.MINE_UDI,
    val udiComission: Double = 0.0,
    val udiValueInMoney: Double = 0.0,
    val udiValueInMoneyCommission: Double = 0.0,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isUdiSaved: Boolean = false,
    val isUdiDeleted: Boolean = false
)

@HiltViewModel
class AddEditUdiViewmodel @Inject constructor(
    private val repository: UdiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val TAG = "AddEditUdiViewmodel"
    }

    private val udiId: String? = savedStateHandle[UdiDestinationArgs.UDI_ID_ARG]
    private val shouldDeleteUdi: String? = savedStateHandle[UdiDestinationArgs.DELETE_ARG]

    private val _uiState = MutableStateFlow(AddEditUdiUiState())
    val uiState: StateFlow<AddEditUdiUiState> = _uiState.asStateFlow()

    init {
        if (udiId != null && shouldDeleteUdi != null && shouldDeleteUdi == "true") {
            deleteUdi(udiId)
        }
        if (udiId != null && shouldDeleteUdi == null) {
            loadUdi(udiId)
        } else if (udiId == null) {
            getUdiForToday(LocalDateTime.now())
        }
    }

    fun saveUdi() {
        if (uiState.value.amount.isEmpty() || uiState.value.date.isEmpty()) {
            _uiState.update {
                it.copy(userMessage = R.string.empty_task_message)
            }
            return
        }

        if (udiId == null) {
            createNewUdi()
        } else {
            updateUdi()
        }
    }

    fun updateAmount(newAmount: String) {
        val totalOfUdi =
            if (newAmount.isEmpty()) 0.0 else newAmount.toDouble() / uiState.value.udiValue.toDouble()
        val udiComission = checkNegativeNumber(totalOfUdi - Constants.MINE_UDI)
        val udiValueInMoney =
            checkNegativeNumber(Constants.MINE_UDI * uiState.value.udiValue.toDouble())
        _uiState.update {
            it.copy(
                amount = newAmount,
                totalOfUdi = totalOfUdi,
                udiComission = udiComission,
                udiValueInMoney = udiValueInMoney,
                udiValueInMoneyCommission = udiComission * uiState.value.udiValue.toDouble()
            )
        }
    }

    fun updateDate(newDate: String) {
        _uiState.update {
            it.copy(date = newDate)
        }
    }

    private fun createNewUdi() = viewModelScope.launch {
        val newUdi = RetirementPlan(
            dateOfPurchase = formatStringToDate(uiState.value.date).atStartOfDay(),
            purchaseTotal = uiState.value.amount.toDouble(),
            udiValue = uiState.value.udiValue.toDouble(),
            totalOfUdi = uiState.value.amount.toDouble() / uiState.value.udiValue.toDouble(),//uiState.value.udiValue.toDouble(),
            mineUdi = Constants.MINE_UDI,
            udiCommission = uiState.value.udiComission,
            udiValueInMoney = uiState.value.udiValueInMoney,
            udiValueInMoneyCommission = uiState.value.udiValueInMoneyCommission
        )
        repository.addUdi(newUdi)
        _uiState.update {
            it.copy(isUdiSaved = true)
        }
    }

    private fun updateUdi() {
        if (udiId == null) {
            throw RuntimeException("updateUdi() was called but task is new.")
        }
        viewModelScope.launch {
            val updatedUdi = RetirementPlan(
                id = udiId.toLong(),
                dateOfPurchase = formatStringToDate(uiState.value.date).atStartOfDay(),
                purchaseTotal = uiState.value.amount.toDouble(),
                udiValue = uiState.value.udiValue.toDouble(),
                totalOfUdi = uiState.value.amount.toDouble() / uiState.value.udiValue.toDouble(),//uiState.value.udiValue.toDouble(),
                mineUdi = Constants.MINE_UDI,
                udiCommission = uiState.value.udiComission,
                udiValueInMoney = uiState.value.udiValueInMoney,
                udiValueInMoneyCommission = uiState.value.udiValueInMoneyCommission
            )

            repository.updateUdiValue(updatedUdi)
            _uiState.update {
                it.copy(isUdiSaved = true)
            }
        }
    }

    private fun loadUdi(udiId: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            repository.getUdiById(udiId.toLong()).let { result ->
                if (result is Result.Success) {
                    val udi = result.data
                    val resultFromApi = getUdiForTodayAsync(udi.dateOfPurchase)
                    if (resultFromApi is Result.Success){
                        Log.d(TAG, "Api result with values ${resultFromApi.data}")
                        val udiValueFromApi = resultFromApi.data.udiValue.toDouble()
                        val totalOfUdi =
                            udi.purchaseTotal / udiValueFromApi
                        val udiComission = checkNegativeNumber(totalOfUdi - Constants.MINE_UDI)
                        val udiValueInMoney =
                            checkNegativeNumber(Constants.MINE_UDI * udiValueFromApi)
                        _uiState.update {
                            it.copy(
                                amount = udi.purchaseTotal.toString(),
                                udiValue = resultFromApi.data.udiValue,
                                date = formatDateForRequest(udi.dateOfPurchase),
                                totalOfUdi = totalOfUdi,
                                udiComission = udiComission,
                                udiValueInMoney = udiValueInMoney,
                                isLoading = false,
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun deleteUdi(udiId: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            repository.getUdiById(udiId.toLong()).let { result ->
                if (result is Result.Success) {
                    val udi = result.data
                    repository.deleteUdi(udi)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isUdiDeleted = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun getUdiForToday(date: LocalDateTime) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        Log.d(TAG, "Calling api... with date $date")
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUdiForToday(date).let { result ->
                if (result is Result.Success) {

                    val udiValue = result.data.udiValue
                    val udiDate = result.data.date
                    _uiState.update {
                        it.copy(
                            udiValue = udiValue,
                            isLoading = false
                        )
                    }
                    Log.d(TAG, "Api success $udiValue with date $udiDate")
                } else {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private suspend fun getUdiForTodayAsync(date: LocalDateTime): Result<UdiItem> {
        val result = viewModelScope.async {
            Log.d(TAG, "Calling api... with date $date")
             repository.getUdiForToday(date)
        }
        return result.await()
    }
}