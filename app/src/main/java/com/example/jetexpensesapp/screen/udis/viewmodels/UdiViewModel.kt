package com.example.jetexpensesapp.screen.udis

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.data.Result.Success
import com.example.jetexpensesapp.data.UdiGlobalDetails
import com.example.jetexpensesapp.model.udi.RetirementPlan
import com.example.jetexpensesapp.model.udi.UdiItem
import com.example.jetexpensesapp.navigation.ADD_EDIT_RESULT_OK
import com.example.jetexpensesapp.navigation.DELETE_RESULT_OK
import com.example.jetexpensesapp.navigation.EDIT_RESULT_OK
import com.example.jetexpensesapp.repository.UdiRepository
import com.example.jetexpensesapp.utils.Async
import com.example.jetexpensesapp.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject

data class UdiHomeUiState(
    val udis: List<RetirementPlan> = emptyList(),
    val globalTotals: UdiGlobalDetails = UdiGlobalDetails(),
    val udiValueToday: String = "0.0",
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)

@HiltViewModel
class UdiViewModel @Inject constructor(
    private val repository: UdiRepository,
    private val savesStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val TAG = "UdiHomeScreenViewModel"
    }

    private val _savedFilterType = savesStateHandle.getStateFlow(
        UDIS_FILTER_SAVED_STATE_KEY,
        UdisDateFilterType.NEW_TO_LAST
    )

    private val _savedUdiValue = savesStateHandle.getStateFlow(UDI_VALUE_FROM_API, "0.0")

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _udisAsync = combine(repository.getAllUdis(), _savedFilterType) { udis, type ->
        filterUdis(udis, type)
    }
        .map { Async.Success(it) }
        .onStart<Async<List<RetirementPlan>>> { emit(Async.Loading) }

    val uiState: StateFlow<UdiHomeUiState> = combine(
        _userMessage, _isLoading, _udisAsync
    ) { userMessage, isLoading, taskAsync ->
        when (taskAsync) {
            Async.Loading -> {
                UdiHomeUiState(isLoading = true)
            }
            is Async.Success -> {
                Log.d(TAG, "value of savedUdiValue ${_savedUdiValue.value}")
                if (_savedUdiValue.value == "0.0") {
                    val udiFromApi = getUdiForTodayAsync(LocalDateTime.now())
                    if (udiFromApi is Success) {
                        savesStateHandle[UDI_VALUE_FROM_API] = udiFromApi.data.udiValue
                        UdiHomeUiState(
                            udis = taskAsync.data,
                            globalTotals = calculateTotals(taskAsync.data, udiFromApi.data.udiValue),
                            udiValueToday = udiFromApi.data.udiValue,
                            isLoading = isLoading,
                            userMessage = userMessage
                        )
                    } else {
                        UdiHomeUiState(isLoading = false)
                    }
                } else {
                    UdiHomeUiState(
                        udis = taskAsync.data,
                        globalTotals = calculateTotals(taskAsync.data, _savedUdiValue.value),
                        udiValueToday = _savedUdiValue.value,
                        isLoading = isLoading,
                        userMessage = userMessage
                    )
                }
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = UdiHomeUiState(isLoading = true)
        )

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    fun showEditResultMessage(result: Int) {
        when (result) {
            EDIT_RESULT_OK -> showSnackBarMessage(R.string.successfully_edited_task_message)
            ADD_EDIT_RESULT_OK -> showSnackBarMessage(R.string.successfully_saved_task_message)
            DELETE_RESULT_OK -> showSnackBarMessage(R.string.successfully_deleted_task_message)
        }
    }

    private fun showSnackBarMessage(message: Int) {
        _userMessage.value = message
    }

    private fun calculateTotals(allUdis: List<RetirementPlan>, udiToday: String): UdiGlobalDetails {
        val udiGlobalDetails = UdiGlobalDetails(
            udiValueToday = udiToday
        )
        for (item in allUdis) {
            udiGlobalDetails.totalExpend += item.purchaseTotal
            udiGlobalDetails.udisTotal += item.totalOfUdi
        }
        udiGlobalDetails.udisConvertion =
            udiToday.toDouble() * udiGlobalDetails.udisTotal

        return udiGlobalDetails
    }

    private fun filterUdis(
        udisResult: Result<List<RetirementPlan>>,
        filteringType: UdisDateFilterType
    ): List<RetirementPlan> = if (udisResult is Success) {
        filterItems(udisResult.data, filteringType)
    } else {
        showSnackBarMessage(R.string.loading_udis_error)
        emptyList()
    }

    private fun filterItems(
        udis: List<RetirementPlan>,
        filteringType: UdisDateFilterType
    ): List<RetirementPlan> =
        when (filteringType) {
            UdisDateFilterType.NEW_TO_LAST -> {
                udis.sortedByDescending { it.dateOfPurchase }
            }
            UdisDateFilterType.LAST_TO_NEW -> {
                udis.sortedBy { it.dateOfPurchase }
            }
        }


    fun setFiltering(requestType: UdisDateFilterType) {
        savesStateHandle[UDIS_FILTER_SAVED_STATE_KEY] = requestType
    }

    private suspend fun getUdiForTodayAsync(date: LocalDateTime): Result<UdiItem> {
        val result = viewModelScope.async {
            Log.d(TAG, "Calling api... with date $date")
            repository.getUdiForToday(date)
        }
        return result.await()
    }
}

const val UDIS_FILTER_SAVED_STATE_KEY = "UDIS_FILTER_SAVED_STATE_KEY"
const val UDI_VALUE_FROM_API = "UDI_VALUE_FROM_API"