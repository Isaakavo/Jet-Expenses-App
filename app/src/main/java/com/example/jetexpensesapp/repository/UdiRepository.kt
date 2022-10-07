package com.example.jetexpensesapp.repository

import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.data.UdiDatabaseDao
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.model.UdiItem
import com.example.jetexpensesapp.network.UdiApi
import com.example.jetexpensesapp.utils.formatDateForRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class UdiRepository @Inject constructor(
    private val api: UdiApi,
    private val udiDatabaseDao: UdiDatabaseDao
) {

    suspend fun getUdiForToday(date: LocalDateTime): Result<UdiItem> = withContext(Dispatchers.IO) {
        try {
            val formatted = formatDateForRequest(date)
            val resultFromRequest = api.getUdiForToday(formatted, formatted).bmx.series[0].datos[0]
            return@withContext Result.Success(resultFromRequest)
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }



    suspend fun addUdi(retirementPlan: RetirementPlan) = udiDatabaseDao.insert(retirementPlan)

    fun getAllUdis(): Flow<Result<List<RetirementPlan>>> =
        udiDatabaseDao.getUdis().map {
            Result.Success(it)
        }

    suspend fun updateUdiValue(retirementPlan: RetirementPlan) {
        udiDatabaseDao.updateUdi(retirementPlan)
    }

    suspend fun getUdiById(id: Long): Result<RetirementPlan> = withContext(Dispatchers.IO) {
        try {
            val udi = udiDatabaseDao.getUdiByID(id)
            if (udi != null) {
                return@withContext Result.Success(udi)
            } else {
                return@withContext Result.Error(Exception("udi not found"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    suspend fun deleteUdi(retirementPlan: RetirementPlan) {
        udiDatabaseDao.deleteUdi(retirementPlan)
    }
}