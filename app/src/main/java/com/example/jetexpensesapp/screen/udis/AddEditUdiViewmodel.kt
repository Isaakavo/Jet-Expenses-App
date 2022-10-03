package com.example.jetexpensesapp.screen.udis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.navigation.UdiDestinationArgs
import com.example.jetexpensesapp.repository.UdiRepository
import com.example.jetexpensesapp.utils.Constants
import com.example.jetexpensesapp.utils.checkNegativeNumber
import com.example.jetexpensesapp.utils.formatDateForRequest
import com.example.jetexpensesapp.utils.formatStringToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class AddEditUdiUiState(
    val amount: String = "",
    val date: String = formatDateForRequest(LocalDateTime.now()),
    val udiValue: String = "",
    val totalOfUdi: Double = 0.0,
    val mineUdi: Double = Constants.MINE_UDI,
    val udiComission: Double = 0.0,
    val udiValueInMoney: Double = 0.0,
    val udiValueInMoneyCommission: Double = 0.0,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isUdiSaved: Boolean = false
)

@HiltViewModel
class AddEditUdiViewmodel @Inject constructor(
    private val repository: UdiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val udiId: String? = savedStateHandle[UdiDestinationArgs.UDI_ID_ARG]

    private val _uiState = MutableStateFlow(AddEditUdiUiState())
    val uiState: StateFlow<AddEditUdiUiState> = _uiState.asStateFlow()

    init {
        if (udiId != null) {
            loadUdi(udiId)
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
        _uiState.update {
            it.copy(
                amount = newAmount,
                totalOfUdi = newAmount.toDouble() / 7.53 ,//uiState.value.udiValue.toDouble(),
                udiComission = checkNegativeNumber(uiState.value.totalOfUdi - Constants.MINE_UDI),
                udiValueInMoney = checkNegativeNumber(Constants.MINE_UDI * 7.53), //uiState.value.udiValue.toDouble()),
                udiValueInMoneyCommission = uiState.value.udiComission *  7.53 //uiState.value.udiValue.toDouble()
            )
        }
    }

    fun updateDate(newDate: String) {
//        if (newDate.length == 10) {
//            date.value = it
//            dateSupp = it
//            //viewModel.getUdiForToday(formatStringToDate(it).atStartOfDay())
//        } else {
//            if (it.length <= 10 && dateSupp.length <= 10) {
//                date.value = it
//            }
//        }
        _uiState.update {
            it.copy(date = newDate)
        }
    }

    private fun createNewUdi() = viewModelScope.launch {
        val newUdi = RetirementPlan(
            dateOfPurchase = formatStringToDate(uiState.value.date).atStartOfDay(),
            purchaseTotal = uiState.value.amount.toDouble(),
            udiValue = 7.53 ,//uiState.value.udiValue.toDouble(),
            totalOfUdi = uiState.value.amount.toDouble() / 7.53,//uiState.value.udiValue.toDouble(),
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
                purchaseTotal = uiState.value.amount.toDouble()
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
                    _uiState.update {
                        it.copy(
                            amount = udi.purchaseTotal.toString(),
                            date = formatDateForRequest(udi.dateOfPurchase),
                            isLoading = false,
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
}