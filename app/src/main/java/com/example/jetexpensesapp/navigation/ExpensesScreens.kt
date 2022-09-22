package com.example.jetexpensesapp.navigation

import androidx.annotation.StringRes
import com.example.jetexpensesapp.R
import java.lang.IllegalArgumentException

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object UdiHomeScreen : Screen("udihome", R.string.udi_home)
    object AddRetirementEntryScreen : Screen("addretiremententry", R.string.add_retirement_entry)
}