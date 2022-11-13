package com.example.jetexpensesapp.data

import androidx.room.*
import com.example.jetexpensesapp.model.udi.RetirementPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface UdiDatabaseDao {
    @Query("Select * from udis")
    fun getUdis(): Flow<List<RetirementPlan>>

    @Query("SELECT * FROM udis WHERE id = :id")
    fun getUdiByID(id: Long): RetirementPlan?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(retirementPlan: RetirementPlan)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUdi(retirementData: RetirementPlan)

    @Delete
    suspend fun deleteUdi(retirementData: RetirementPlan)
}