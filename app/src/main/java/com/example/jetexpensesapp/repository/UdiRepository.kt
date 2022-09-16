package com.example.jetexpensesapp.repository

import android.util.Log
import com.example.jetexpensesapp.model.UDI
import com.example.jetexpensesapp.network.UdiApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UdiRepository @Inject constructor(private val api: UdiApi){

    suspend fun getUdiForToday(): UDI {
        var udi = UDI()
        try {
            val currentDate = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = currentDate.format(formatter)
            udi = api.getUdiForToday(formatted, formatted)
        } catch (e: Exception) {
            Log.d("Repository", "getUdiForToday ${e.localizedMessage}")
        }

        return udi
    }
}