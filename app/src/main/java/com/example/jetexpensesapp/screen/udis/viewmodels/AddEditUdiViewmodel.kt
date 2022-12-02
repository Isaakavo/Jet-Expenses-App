package com.example.jetexpensesapp.screen.udis

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.model.udi.Data
import com.example.jetexpensesapp.model.udi.RetirementRecord
import com.example.jetexpensesapp.model.udi.UdiItem
import com.example.jetexpensesapp.navigation.UdiDestinationArgs
import com.example.jetexpensesapp.repository.UdiRepository
import com.example.jetexpensesapp.utils.*
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
    val date: String = formatDateForUi(LocalDateTime.now()),
    val dateForRequest: LocalDateTime? = LocalDateTime.now(),
    val udiValue: String = "0.0",
    val udiCommission: String = "0.0",
    val totalOfUdi: Double? = 0.0,
    val mineUdi: Double? = Constants.MINE_UDI,
    val udiComission: Double? = 0.0,
    val udiValueInMoney: Double? = 0.0,
    val udiValueInMoneyCommission: Double? = 0.0,
    val dataObj: Data = Data(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isUdiSaved: Boolean = false,
    val isUdiDeleted: Boolean = false,
    val shouldDisplayBottomSheet: Boolean = false
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
            it.copy(
                date = formatDateForUi(newDate.toLocalDateTime()),
                dateForRequest = newDate.toLocalDateTime()
            )
        }
    }

    private fun createNewUdi() = viewModelScope.launch {
        val newUdi = RetirementRecord(
            id = 0,
            dateOfPurchase = uiState.value.dateForRequest.toString(),
            purchaseTotal = uiState.value.amount.toDouble(),
            udiValue = uiState.value.udiValue.toDouble()
        )
        val newValueAdded = repository.insertUdiToApi(newUdi)
        if (newValueAdded.status != "SUCCESS") {
            _uiState.update {
                it.copy(isUdiSaved = true, userMessage = R.string.error_insert)
            }
        } else {
            _uiState.update {
                it.copy(isUdiSaved = true)
            }
        }
    }

    private fun updateUdi() {
        if (udiId == null) {
            throw RuntimeException("updateUdi() was called but task is new.")
        }
        viewModelScope.launch {
            val updateUdi = RetirementRecord(
                dateOfPurchase = uiState.value.dateForRequest.toString(),
                purchaseTotal = uiState.value.amount.toDouble(),
                udiValue = uiState.value.udiValue.toDouble()
            )
            val response = repository.updateUdiById(udiId.toLong(), updateUdi)
            if (response.status != "SUCCESS") {
                _uiState.update {
                    it.copy(isUdiSaved = false)
                }
            } else {
                _uiState.update {
                    it.copy(isUdiSaved = true)
                }
            }
        }
    }

    private fun loadUdi(udiId: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            repository.getUdiByIdFromApi(udiId.toLong()).let { result ->
                if (result is Result.Success) {
                    val udi = result.data.body.data[0]
                    _uiState.update {
                        it.copy(
                            amount = udi.retirementRecord?.purchaseTotal.toString(),
                            udiValue = udi.retirementRecord?.udiValue.toString(),
                            date = formatDateFromServer(udi.retirementRecord?.dateOfPurchase),
                            dateForRequest = udi.retirementRecord?.dateOfPurchase?.toLocalDateTime(),
                            totalOfUdi = udi.udiConversions?.udiConversion,
                            udiComission = udi.retirementRecord?.udiCommission?.userUdis,
                            udiValueInMoney = udi.udiConversions?.udiConversion,
                            isLoading = false,
                            shouldDisplayBottomSheet = true,
                            dataObj = udi
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

    private fun deleteUdi(udiId: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val response = repository.deleteUdiFromServer(udiId.toLong())
            if (response.status == "SUCCESS") {
                _uiState.update {
                    it.copy(isLoading = false, isUdiDeleted = true)
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false)
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