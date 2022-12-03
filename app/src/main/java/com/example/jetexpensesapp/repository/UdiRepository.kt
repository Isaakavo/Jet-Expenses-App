package com.example.jetexpensesapp.repository

import com.example.jetexpensesapp.data.Result
import com.example.jetexpensesapp.data.UdiDatabaseDao
import com.example.jetexpensesapp.model.udi.*
import com.example.jetexpensesapp.network.UdiApi
import com.example.jetexpensesapp.network.UdiEndpoint
import com.example.jetexpensesapp.utils.formatDateForRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class UdiRepository @Inject constructor(
    private val api: UdiApi,
    private val udiEndpoint: UdiEndpoint,
    private val udiDatabaseDao: UdiDatabaseDao
) {

    suspend fun getUdiForToday(date: LocalDateTime): Result<UdiItem> = withContext(Dispatchers.IO) {
        try {
            val formatted = formatDateForRequest(date)
            val resultFromRequest = api.getUdiForToday(formatted, formatted).bmx.series[0].datos[0]
            return@withContext Result.Success(resultFromRequest)
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }


    suspend fun addUdi(retirementPlan: RetirementPlan) = udiDatabaseDao.insert(retirementPlan)

    suspend fun insertUdiToApi(retirementRecord: RetirementRecord): ServerResponse =
        udiEndpoint.insertUdi(retirementRecord)

    fun getAllUdis(): Flow<Result<List<RetirementPlan>>> =
        udiDatabaseDao.getUdis().map {
            Result.Success(it)
        }

    fun getAllUdisFromEndpoint(): Flow<Result<ServerResponse>> {
        return flow {
            emit(udiEndpoint.getAllUdis())
        }.map {
            Result.Success(it)
        }
    }

    suspend fun getAllUdisFrom(): Result<ServerResponse> {
        return try {
            val data = udiEndpoint.getAllUdis()
            if (data.status == "SUCCESS") {
                Result.Success(data)
            } else {
                Result.Error(data.status)
            }
        } catch (e: Exception) {
            val error = e.localizedMessage
            val serverResponse = ServerResponse()
            if (error != null) {
                serverResponse.status = error
            }
            Result.Error(serverResponse.status)
        }
    }

    suspend fun updateUdiValue(retirementPlan: RetirementPlan) {
        udiDatabaseDao.updateUdi(retirementPlan)
    }

    suspend fun updateUdiById(id: Long, retirementRecord: RetirementRecord) =
        udiEndpoint.updateUdi(id, retirementRecord)

    suspend fun getUdiById(id: Long): Result<RetirementPlan> = withContext(Dispatchers.IO) {
        try {
            val udi = udiDatabaseDao.getUdiByID(id)
            if (udi != null) {
                return@withContext Result.Success(udi)
            } else {
                return@withContext Result.Error(Exception("udi not found").localizedMessage)
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun getUdiByIdFromApi(id: Long): Result<ServerResponse> = withContext(Dispatchers.IO) {
        try {
            val udi = udiEndpoint.getUdiById(id)
            if (udi != null) {
                return@withContext Result.Success(udi)
            } else {
                return@withContext Result.Error(Exception("udi not found").localizedMessage)
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun deleteUdi(retirementPlan: RetirementPlan) {
        udiDatabaseDao.deleteUdi(retirementPlan)
    }

    suspend fun deleteUdiFromServer(id: Long): ServerResponse = udiEndpoint.deleteUdi(id)

    suspend fun getCommission(): Result<ServerResponse> {
        return try {
            val commission = udiEndpoint.getCommission()
            if (commission.status != "SUCCESS") {
                Result.Error(Exception("No data").localizedMessage)
            } else {
                Result.Success(commission)
            }

        } catch (e: Exception) {
            Result.Error(e.localizedMessage)
        }

    }

    suspend fun insertCommission(data: UdiCommissionPost): Result<ServerResponse> {
        return try {
            val commission = udiEndpoint.insertCommission(data)
            if (commission.status != "SUCCESS") {
                Result.Error(Exception("No data").localizedMessage)
            } else {
                Result.Success(commission)
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage)
        }
    }
}