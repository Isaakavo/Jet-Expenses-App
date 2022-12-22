package com.example.jetexpensesapp.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.DELETE_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.GLOBAL_VALUES
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.IS_INSERT_COMMISSION
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.TITLE_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.UDI_ID_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.USER_MESSAGE_ARG
import com.example.jetexpensesapp.navigation.UdiScreens.ADD_EDIT_COMMISSIONS_SCREEN
import com.example.jetexpensesapp.navigation.UdiScreens.ADD_EDIT_UDI_SCREEN
import com.example.jetexpensesapp.navigation.UdiScreens.UDI_GLOBAL_DETAIL_SCREEN
import com.example.jetexpensesapp.navigation.UdiScreens.UDI_HOME_SCREEN

object UdiScreens {
    const val UDI_HOME_SCREEN = "udis"
    const val UDI_GLOBAL_DETAIL_SCREEN = "udiGlobalDetail"
    const val ADD_EDIT_UDI_SCREEN = "addEditUdi"
    const val ADD_EDIT_COMMISSIONS_SCREEN = "addEditCommissionsScreen"
}

object UdiDestinationArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val UDI_ID_ARG = "udiId"
    const val COMMISSION_ID_ARG = "true"
    const val IS_INSERT_COMMISSION = "isInsertCommission"
    const val TITLE_ARG = "title"
    const val DELETE_ARG = "delete"
    const val GLOBAL_VALUES = "globalValues"
}

object UdisDestination {
    const val LOGIN_SCREEN_ROUTE = "login"
    const val UDI_HOMESCREEN_ROUTE = "$UDI_HOME_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val ADD_EDIT_TASK_ROUTE =
        "$ADD_EDIT_UDI_SCREEN/{$TITLE_ARG}?$UDI_ID_ARG={$UDI_ID_ARG}&$DELETE_ARG={$DELETE_ARG}"
    const val ADD_EDIT_COMMISSION_ROUTE =
        "$ADD_EDIT_COMMISSIONS_SCREEN/{$TITLE_ARG}?$IS_INSERT_COMMISSION={$IS_INSERT_COMMISSION}"
    const val UDI_GLOBAL_DETAIL = "$UDI_GLOBAL_DETAIL_SCREEN?{$GLOBAL_VALUES}"
}

class UdiNavigationActions(private val navHostController: NavHostController) {

    //TODO handle logout on back button
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
            launchSingleTop = false
            restoreState = true
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

    fun navigateToAddEditCommissionEntry(
        title: Int,
        commissionId: String?,
        isInsertCommission: Boolean?
    ) {
        navHostController.navigate(
//            "$ADD_EDIT_COMMISSIONS_SCREEN/$title".let {
//                if (isInsertCommission != null) "$it?$IS_INSERT_COMMISSION=$isInsertCommission"
//                else it
//            }
            "$ADD_EDIT_COMMISSIONS_SCREEN/$title?$IS_INSERT_COMMISSION=$isInsertCommission"
        )
    }

    fun navigateToUdiGlobalDetails(values: String) {
        navHostController.navigate(
            "$UDI_GLOBAL_DETAIL_SCREEN?$GLOBAL_VALUES=$values"
        )
    }
}