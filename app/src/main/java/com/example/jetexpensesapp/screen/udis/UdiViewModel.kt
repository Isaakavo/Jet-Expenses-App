package com.example.jetexpensesapp.screen.udis

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.data.DataOrException
import com.example.jetexpensesapp.data.UdiGlobalDetails
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.model.UdiItem
import com.example.jetexpensesapp.navigation.ADD_EDIT_RESULT_OK
import com.example.jetexpensesapp.navigation.DELETE_RESULT_OK
import com.example.jetexpensesapp.navigation.EDIT_RESULT_OK
import com.example.jetexpensesapp.repository.UdiRepository
import com.example.jetexpensesapp.utils.Async
import com.example.jetexpensesapp.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

data class UdiUiState(
    val udis: List<RetirementPlan> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)

@HiltViewModel
class UdiViewModel @Inject constructor(
    private val repository: UdiRepository,
    private val savesStateHandle: SavedStateHandle
) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _udisAsync = repository.getAllUdis().map {
        Async.Success(it)
    }
        .onStart<Async<List<RetirementPlan>>> { emit(Async.Loading) }

    val uiState: StateFlow<UdiUiState> = combine(
        _userMessage, _isLoading, _udisAsync
    ) { userMessage, isLoading, taskAsync ->
        when (taskAsync) {
            Async.Loading -> {
                UdiUiState(isLoading = true)
            }
            is Async.Success -> {
                UdiUiState(
                    udis = taskAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = UdiUiState(isLoading = true)
        )

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    fun showEditResultMessage(result: Int) {
        when (result) {
            EDIT_RESULT_OK -> showSnackBarMessage(R.string.successfully_saved_task_message)
            ADD_EDIT_RESULT_OK -> showSnackBarMessage(R.string.successfully_added_task_message)
            DELETE_RESULT_OK -> showSnackBarMessage(R.string.successfully_deleted_task_message)
        }
    }

    private fun showSnackBarMessage(message: Int) {
        _userMessage.value = message
    }

    private val _dataFromDb = MutableStateFlow<List<RetirementPlan>>(emptyList())
    val dataFromDb = _dataFromDb.asStateFlow()

    private val _singleRetirementPlan = MutableStateFlow<RetirementPlan?>(null)
    val singleRetirementPlan = _singleRetirementPlan.asStateFlow()

    var udiFromApi by mutableStateOf<DataOrException<UdiItem, Boolean, Exception>>(
        DataOrException(
            null,
            false,
            Exception("")
        )
    )

    var globalValues by mutableStateOf(UdiGlobalDetails(0.0, 0.0, 0.0))

    init {
        if (udiFromApi.data == null) {
            getUdiForToday(LocalDateTime.now())
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUdis().distinctUntilChanged().collect { udis ->
                val ordered = udis.sortedBy { it.dateOfPurchase }
                _dataFromDb.value = ordered
                withContext(Dispatchers.Main) {
                    getUdiGlobalDetails()
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

    private fun getUdiGlobalDetails() {
        globalValues.totalExpend = 0.0
        globalValues.udisTotal = 0.0
        _dataFromDb.value.map {
            globalValues.totalExpend += it.purchaseTotal
            globalValues.udisTotal += it.totalOfUdi
        }
    }


    fun addUdi(retirementPlan: RetirementPlan) =
        viewModelScope.launch {
            repository.addUdi(retirementPlan)
        }

    fun updateUdiValue(retirementPlan: RetirementPlan) {
        viewModelScope.launch {
            repository.updateUdiValue(retirementPlan)
        }
    }

    fun getUdiById(id: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getUdiById(id).distinctUntilChanged().collect {
                _singleRetirementPlan.value = it
            }
        }
    }

    fun deleteUdi(retirementPlan: RetirementPlan) {
        viewModelScope.launch {
            repository.deleteUdi(retirementPlan)
            getUdiGlobalDetails()
        }
    }

    fun resetSingleRetirementPlan() {
        viewModelScope.launch {
            _singleRetirementPlan.value = null
        }
    }
}