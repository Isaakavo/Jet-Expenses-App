package com.example.jetexpensesapp.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetexpensesapp.data.DataOrException
import com.example.jetexpensesapp.model.UdiItem
import com.example.jetexpensesapp.repository.UdiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UdiViewModel @Inject constructor(private val repository: UdiRepository) : ViewModel() {
    var data: MutableState<DataOrException<UdiItem, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        getUdiForToday()
    }

    private fun getUdiForToday() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getUdiForToday()
            if (data.value.data.toString().isNotEmpty()) {
                data.value.loading = false
            }
        }
    }
}