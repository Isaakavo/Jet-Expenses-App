package com.example.jetexpensesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetexpensesapp.model.RetirementPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface UdiDatabaseDao {
    @Query("Select * from udis")
    fun getUdis(): Flow<List<RetirementPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(retirementPlan: RetirementPlan)
}