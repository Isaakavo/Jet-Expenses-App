package com.example.jetexpensesapp.utils

import android.icu.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun formatDate(time: LocalDateTime): String {
    //val date = Date(time)
    val format = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
    return time.format(format)
}

fun formatDateForRequest(time: LocalDateTime): String {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    return time.format(format)
}

fun formatStringToDate(str: String): LocalDate {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    return LocalDate.parse(str, format)
}

fun checkNegativeNumber(quantity: Double): Double {
    return if (quantity < 0) 0.0 else quantity
}