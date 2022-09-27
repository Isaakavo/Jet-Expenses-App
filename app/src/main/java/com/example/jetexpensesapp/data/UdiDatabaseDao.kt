package com.example.jetexpensesapp.data

import androidx.room.*
import com.example.jetexpensesapp.model.RetirementPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface UdiDatabaseDao {
    @Query("Select * from udis")
    fun getUdis(): Flow<List<RetirementPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(retirementPlan: RetirementPlan)

    @Update
    suspend fun updateUdi(retirementData: RetirementPlan)

    @Delete
    suspend fun deleteUdi(retirementData: RetirementPlan)
}