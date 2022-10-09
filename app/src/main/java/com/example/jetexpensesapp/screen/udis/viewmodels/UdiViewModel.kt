package com.example.jetexpensesapp.screen.udis

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.data.Result.Success
import com.example.jetexpensesapp.data.UdiGlobalDetails
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.navigation.ADD_EDIT_RESULT_OK
import com.example.jetexpensesapp.navigation.DELETE_RESULT_OK
import com.example.jetexpensesapp.navigation.EDIT_RESULT_OK
import com.example.jetexpensesapp.repository.UdiRepository
import com.example.jetexpensesapp.utils.Async
import com.example.jetexpensesapp.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class UdiHomeUiState(
    val udis: List<RetirementPlan> = emptyList(),
    val globalTotals: UdiGlobalDetails = UdiGlobalDetails(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)

@HiltViewModel
class UdiViewModel @Inject constructor(
    private val repository: UdiRepository,
    private val savesStateHandle: SavedStateHandle
) : ViewModel() {

    private val _savedFilterType = savesStateHandle.getStateFlow(
        UDIS_FILTER_SAVED_STATE_KEY,
        UdisDateFilterType.NEW_TO_LAST
    )

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val globalTotals = MutableStateFlow(UdiGlobalDetails())
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
                UdiHomeUiState(
                    udis = taskAsync.data,
                    globalTotals = calculateTotals(taskAsync.data),
                    isLoading = isLoading,
                    userMessage = userMessage
                )
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

    private fun calculateTotals(allUdis: List<RetirementPlan>): UdiGlobalDetails {
        val udiGlobalDetails = UdiGlobalDetails()
        for (item in allUdis) {
            udiGlobalDetails.totalExpend += item.purchaseTotal
            udiGlobalDetails.udisTotal += item.totalOfUdi
            udiGlobalDetails.udisConvertion += item.purchaseTotal * item.totalOfUdi
        }

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

    var globalValues by mutableStateOf(UdiGlobalDetails(0.0, 0.0, 0.0))
}

const val UDIS_FILTER_SAVED_STATE_KEY = "UDIS_FILTER_SAVED_STATE_KEY"