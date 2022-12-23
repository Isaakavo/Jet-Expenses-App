package com.example.jetexpensesapp.utils

import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun formatDateForRequest(time: LocalDateTime): String {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    return time.format(format)
}

fun formatDateForUi(time: LocalDateTime): String {
    val format = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return time.format(format)
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
        maximumFractionDigits = 2
    }
    return if (number != null) format.format(number) else ""
}

fun String.capitalLetterForDate(): String {
    val arr = this.split(" ")
    val firstLetter = arr[1][0].uppercase()
    val newString = arr[1].drop(1)
    return "${arr[0]} $firstLetter$newString ${arr[2]}"
}

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this)
}