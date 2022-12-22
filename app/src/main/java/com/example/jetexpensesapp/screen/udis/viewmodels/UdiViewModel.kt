package com.example.jetexpensesapp.screen.udis

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.data.Result.Success
import com.example.jetexpensesapp.data.UdiGlobalDetails
import com.example.jetexpensesapp.model.udi.*
import com.example.jetexpensesapp.navigation.ADD_EDIT_RESULT_OK
import com.example.jetexpensesapp.navigation.DELETE_RESULT_OK
import com.example.jetexpensesapp.navigation.EDIT_RESULT_OK
import com.example.jetexpensesapp.repository.UdiRepository
import com.example.jetexpensesapp.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class UdiHomeUiState(
    val udis: List<Data> = emptyList(),
    val commission: UdiBonus? = null,
    val globalTotals: UdiGlobalDetails = UdiGlobalDetails(),
    val udiValueToday: String = "0.0",
    val isLoading: Boolean = false,
    var userMessage: Int? = null,
    val isError: Boolean = false,
    val errorMessage: String = ""
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
    private val _uiData = MutableStateFlow<UdiHomeUiState>(UdiHomeUiState(isLoading = true))
    val uiState: StateFlow<UdiHomeUiState> = _uiData.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = UdiHomeUiState(isLoading = true)
    )

    init {
        //TODO implement filter udis by date
        /**
        View model to call udi api to get all the list of udis from DB
         */
        getAllUdis()

        /**
        View model to call banxico api to get the value of the day for the day
         */
        viewModelScope.launch(Dispatchers.IO) {
            if (_savedUdiValue.value == "0.0") {
                val udiFromApi = repository.getUdiForToday(LocalDateTime.now())
                if (udiFromApi is Success) {
                    savesStateHandle[UDI_VALUE_FROM_API] = udiFromApi.data.udiValue
                    _uiData.update {
                        it.copy(udiValueToday = udiFromApi.data.udiValue)
                    }
                    val globalDetails = repository.getGlobalDetails(udiFromApi.data.udiValue)
                    if (globalDetails is Success) {
                        val data = globalDetails.data.body.data
                        Log.d("TEFSA", "Value $data")
                        _uiData.update {
                            it.copy(
                                globalTotals = UdiGlobalDetails(
                                    totalExpend = data[0].totalExpend,
                                    udisTotal = data[0].udisTotal,
                                    udisConvertion = data[0].udisConvertion,
                                    rendimiento = data[0].rendimiento,
                                    udiValueToday = udiFromApi.data.udiValue,
                                    udiBonus = data[0].udiBonus
                                )
                            )
                        }
                    } else {
                        Log.d("ead", "adsd")
                    }
                } else {
                    _uiData.value = UdiHomeUiState(isLoading = false)
                }
            }
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
        _uiData.update {
            it.copy(userMessage = null)
        }
    }

    fun showEditResultMessage(result: Int) {
        when (result) {
            EDIT_RESULT_OK -> showSnackBarMessage(R.string.successfully_edited_task_message)
            ADD_EDIT_RESULT_OK -> showSnackBarMessage(R.string.successfully_saved_task_message)
            DELETE_RESULT_OK -> showSnackBarMessage(R.string.successfully_deleted_task_message)
        }
    }

    fun getAllUdis() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val dataFromEndpoint = repository.getAllUdisFrom()) {
                is Success -> {
                    val dataReceived = dataFromEndpoint.data.body.data
                    if (_uiData.value.udis.size != dataReceived.size) {
                        _uiData.update {
                            it.copy(
                                udis = dataReceived,
                                isLoading = false
                            )
                        }
                    } else {
                        _uiData.update {
                            it.copy(
                                isLoading = false,
                                userMessage = R.string.api_no_new_data,
                                isError = false
                            )
                        }
                    }

                }
                is Result.Error -> {
                    when(dataFromEndpoint.exception) {
                        "NO_DATA" -> {
                            _uiData.update {
                                it.copy(
                                    isLoading = false,
                                    isError = false,
                                    userMessage = R.string.no_data,
                                    errorMessage = dataFromEndpoint.exception
                                )
                            }
                        }
                        else -> {
                            _uiData.update {
                                it.copy(
                                    isLoading = false,
                                    isError = true,
                                    userMessage = R.string.api_error,
                                    errorMessage = dataFromEndpoint.exception!!
                                )
                            }
                            showSnackBarMessage(R.string.api_error)
                        }
                    }
                }
            }
        }
    }

    private fun showSnackBarMessage(message: Int) {
        _userMessage.value = message
    }

    private fun filterUdis(
        udisResult: Result<ServerResponse>,
        filteringType: UdisDateFilterType
    ): List<Data> = if (udisResult is Success) {
        filterItems(udisResult.data.body as Body, filteringType)
    } else {
        showSnackBarMessage(R.string.loading_udis_error)
        emptyList()
    }

    private fun filterItems(
        udis: Body,
        filteringType: UdisDateFilterType
    ): List<Data> =
        when (filteringType) {
            UdisDateFilterType.NEW_TO_LAST -> {
                udis.data.sortedByDescending { it.retirementRecord?.dateOfPurchase }
            }
            UdisDateFilterType.LAST_TO_NEW -> {
                udis.data.sortedBy { it.retirementRecord?.dateOfPurchase }
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