package com.example.jetexpensesapp.components.shared

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.time.LocalTime
import java.util.*


object DatePicker {
    // Initializing a Calendar
    fun getDatePicker(context: Context, onRequest: (String) -> Unit = {}): DatePickerDialog {
        val mCalendar = Calendar.getInstance()

        // Fetching current year, month and day
        val mYear = mCalendar.get(Calendar.YEAR)
        val mMonth = mCalendar.get(Calendar.MONTH)
        val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

        mCalendar.time = Date()

        return DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, mDayOfMonth: Int ->
                var monthTemp = "${month + 1}"
                var dayTemp = "$mDayOfMonth"
                if (!monthTemp.matches(Regex("(0[1-9]|[12][0-9]|3[01])"))) {
                    monthTemp = "0${month + 1}"
                }
                if (!dayTemp.matches(Regex("(0[1-9]|[12][0-9]|3[01])"))) {
                    dayTemp = "0$dayTemp"
                }
                val time = LocalTime.now()
                val requestDate = "$year-${monthTemp}-${dayTemp}T$time"
                onRequest(requestDate)
            }, mYear, mMonth, mDay
        )
    }

}