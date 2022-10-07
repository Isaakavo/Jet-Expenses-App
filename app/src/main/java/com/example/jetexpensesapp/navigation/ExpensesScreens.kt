package com.example.jetexpensesapp.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.DELETE_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.TITLE_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.UDI_ID_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.USER_MESSAGE_ARG
import com.example.jetexpensesapp.navigation.UdiScreens.ADD_EDIT_UDI_SCREEN
import com.example.jetexpensesapp.navigation.UdiScreens.UDI_HOME_SCREEN

private object UdiScreens {
    const val UDI_HOME_SCREEN = "udis"
    const val UDI_DETAIL_SCREEN = "udi"
    const val ADD_EDIT_UDI_SCREEN = "addEditUdi"
}

object UdiDestinationArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val UDI_ID_ARG = "udiId"
    const val TITLE_ARG = "title"
    const val DELETE_ARG = "delete"
}

object UdisDestination {
    const val UDI_HOMESCREEN_ROUTE = "$UDI_HOME_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val ADD_EDIT_TASK_ROUTE = "$ADD_EDIT_UDI_SCREEN/{$TITLE_ARG}?$UDI_ID_ARG={$UDI_ID_ARG}&$DELETE_ARG={$DELETE_ARG}"
}

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector? = null
) {
    object UdiHomeScreen : Screen("udihome", R.string.udi_home, Icons.Filled.ShowChart)
    object AddRetirementEntryScreen :
        Screen("addretiremententry", R.string.add_retirement_entry)

    object UdiGlobalDetailsScreen : Screen("udiDetails", R.string.global_details)
}

class UdiNavigationActions(private val navHostController: NavHostController) {

    fun navigateToHome(userMessage: Int = 0) {
        val navigateFrom = userMessage == 0
        navHostController.navigate(
            UDI_HOME_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                inclusive = !navigateFrom
                saveState = navigateFrom
            }
            launchSingleTop = true
            restoreState = navigateFrom
        }
    }

    fun navigateToAddEditUdiEntry(title: Int, udiId: String?) {
        navHostController.navigate(
            "$ADD_EDIT_UDI_SCREEN/$title".let {
                if (udiId != null) "$it?$UDI_ID_ARG=$udiId" else it
            }
        )
    }

    fun navigateToDeleteUdiEntry(title: Int, udiId: String, shouldDelete: Boolean) {
        navHostController.navigate(
            "$ADD_EDIT_UDI_SCREEN/$title?$UDI_ID_ARG=$udiId&$DELETE_ARG=$shouldDelete"
        )
    }
}