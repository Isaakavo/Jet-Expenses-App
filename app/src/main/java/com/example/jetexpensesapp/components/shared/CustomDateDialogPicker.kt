package com.example.jetexpensesapp.components.shared

import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.utils.capitalLetterForDate
import com.example.jetexpensesapp.utils.formatDateForRequest
import com.example.jetexpensesapp.utils.formatDateForUi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun DatePicker(
    date: String,
    onDateSelected: (String) -> Unit, onDismissRequest: () -> Unit
) {
    val dateToFormat = "$date 00:00:00"
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", Locale.getDefault())
    val selDate = remember { mutableStateOf(LocalDateTime.parse(dateToFormat, formatter)) }
    val basePadding = 6.dp

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.select_date),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(basePadding))

                Text(
                    text = formatDateForUi(selDate.value).capitalLetterForDate(),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(basePadding))
            }

            CustomCalendarView(
                selDate.value,
                onDateSelected = {
                    selDate.value = it
                })

            Spacer(modifier = Modifier.size(basePadding))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = basePadding, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.secondary
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(formatDateForRequest(selDate.value))
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.accept),
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.secondary
                    )
                }

            }
        }
    }
}

@Composable
fun CustomCalendarView(desiredDate: LocalDateTime, onDateSelected: (LocalDateTime) -> Unit) {
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.Theme_JetExpensesApp))
        },
        update = { view ->

            view.date = getTimeInMillis(desiredDate)
            view.maxDate = getTimeInMillis(LocalDateTime.now().plusDays(15))// contraints

            view.setOnDateChangeListener { _, mYear, mMonth, dayOfMonth ->
                onDateSelected(
                    LocalDateTime
                        .now()
                        .withMonth(mMonth + 1)
                        .withYear(mYear)
                        .withDayOfMonth(dayOfMonth)
                )
            }
        }
    )
}

fun getTimeInMillis(desiredDate: LocalDateTime): Long {
    val calendar = Calendar.getInstance()
    val year = desiredDate.year
    val month = desiredDate.monthValue - 1
    val day = desiredDate.dayOfMonth

    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, day)

    return calendar.timeInMillis
}