package com.example.jetexpensesapp.utils

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun formatDate(time: LocalDateTime): String {
    var format = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())

    when (time.year) {
        LocalDateTime.now().year -> {
            format = DateTimeFormatter.ofPattern("dd MMMM", Locale.getDefault())
        }
    }
    return time.format(format)
}

fun formatDateForRequest(time: LocalDateTime): String {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    return time.format(format)
}

fun formatDateNoYear(time: LocalDateTime): String {
    val format = DateTimeFormatter.ofPattern("dd MMM yy", Locale.getDefault())
    return time.format(format)
}

fun formatDateFullMonth(time: LocalDateTime): String {
    val format = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return time.format(format)
}

fun formatStringToDate(str: String): LocalDate {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    return LocalDate.parse(str, format)
}

fun formatDateFromServer(date: String?): String {
    if (date == null) return ""
    val localDateTime = LocalDateTime.parse(date)
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return formatter.format(localDateTime)
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
    }
    return if (number != null) format.format(number) else ""
}