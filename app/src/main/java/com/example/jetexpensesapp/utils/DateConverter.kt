package com.example.jetexpensesapp.utils

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long): LocalDateTime? {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong), TimeZone.getDefault().toZoneId())
    }

    @TypeConverter
    fun fromDate(date: LocalDateTime?): Long {
        val zdt = ZonedDateTime.of(date, ZoneId.systemDefault())
        return zdt.toInstant().toEpochMilli()
    }
}