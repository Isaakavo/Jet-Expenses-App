package com.example.jetexpensesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.utils.DateConverter


@Database(
    entities = [RetirementPlan::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class UdiDatabase : RoomDatabase() {
    abstract fun udiDao(): UdiDatabaseDao
}