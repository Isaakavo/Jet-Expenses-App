package com.example.jetexpensesapp.utils

import android.icu.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun formatDate(time: LocalDateTime): String {
    //val date = Date(time)
    val format = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
    return time.format(format)
}