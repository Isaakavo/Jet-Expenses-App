package com.example.jetexpensesapp.repository

import android.util.Log
import com.example.jetexpensesapp.data.DataOrException
import com.example.jetexpensesapp.data.UdiDatabaseDao
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.model.UdiItem
import com.example.jetexpensesapp.network.UdiApi
import com.example.jetexpensesapp.utils.formatDateForRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UdiRepository @Inject constructor(
    private val api: UdiApi,
    private val udiDatabaseDao: UdiDatabaseDao
) {
    private val dataOrException = DataOrException<UdiItem, Boolean, Exception>()

    suspend fun getUdiForToday(date: LocalDateTime): DataOrException<UdiItem, Boolean, Exception> {
        try {
            dataOrException.loading = true
            val formatted = formatDateForRequest(date)
            dataOrException.data = api.getUdiForToday(formatted, formatted).bmx.series[0].datos[0]
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            Log.d("Repository", "getUdiForToday ${e.localizedMessage}")
        }
        return dataOrException
    }

    suspend fun addUdi(retirementPlan: RetirementPlan) = udiDatabaseDao.insert(retirementPlan)

    fun getAllUdis(): Flow<List<RetirementPlan>> =
        udiDatabaseDao.getUdis().flowOn(Dispatchers.IO).conflate()
}