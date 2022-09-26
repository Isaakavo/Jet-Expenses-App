package com.example.jetexpensesapp.utils

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun formatDate(time: LocalDateTime): String {
    //val date = Date(time)
    val format = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
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

fun formatNumber(number: Double?): String {
    return if (number != null) String.format("%.2f", number) else ""
}

fun formatMoney(number: Double?): String {
    val format = NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 0
        //currency = Currency.getInstance("MXN")
    }
    return if (number != null) format.format(number) else ""
}