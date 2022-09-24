package com.example.jetexpensesapp.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jetexpensesapp.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector? = null) {
    object UdiHomeScreen : Screen("udihome", R.string.udi_home, Icons.Filled.ShowChart)
    object AddRetirementEntryScreen :
        Screen("addretiremententry", R.string.add_retirement_entry)
}