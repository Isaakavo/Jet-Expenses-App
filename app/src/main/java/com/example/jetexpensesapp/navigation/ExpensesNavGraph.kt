package com.example.jetexpensesapp.navigation

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetexpensesapp.R
import com.example.jetexpensesapp.components.UdiBottomSheet
import com.example.jetexpensesapp.components.UdiEntryDetails
import com.example.jetexpensesapp.components.UdiHomeScreen
import com.example.jetexpensesapp.components.shared.Button
import com.example.jetexpensesapp.components.shared.ButtonVariants
import com.example.jetexpensesapp.data.UdiGlobalDetails
import com.example.jetexpensesapp.model.udi.Data
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.COMMISSION_ID_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.DELETE_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.GLOBAL_VALUES
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.IS_INSERT_COMMISSION
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.TITLE_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.UDI_ID_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.USER_MESSAGE_ARG
import com.example.jetexpensesapp.screen.login.LoginScreen
import com.example.jetexpensesapp.screen.udis.AddRetirementEntryScreen
import com.example.jetexpensesapp.screen.udis.UdiGlobalDetailsScreen
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpensesNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    bottomSheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
    showModalSheet: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    },
    startDestination: String = UdisDestination.LOGIN_SCREEN_ROUTE,
    navActions: UdiNavigationActions = remember(navController) {
        UdiNavigationActions(navController)
    }
) {
    val currentNAvBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNAvBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(
            UdisDestination.LOGIN_SCREEN_ROUTE
        ) {
            LoginScreen(
                navigate = { value ->
                    when (value) {
                        UdiScreens.UDI_HOME_SCREEN -> {
                            navActions.navigateToHome()
                        }
                        UdiScreens.ADD_EDIT_COMMISSIONS_SCREEN -> {
                            navActions.navigateToAddEditCommissionEntry(
                                R.string.add_commission,
                                null,
                                true
                            )
                        }
                    }
                }
            )
        }

        composable(
            UdisDestination.UDI_HOMESCREEN_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) { type = NavType.IntType; defaultValue = 0 }
            )
        ) { entry ->

            var data by remember { mutableStateOf(Data()) }

            fun hideSheet() {
                if (bottomSheetState.isVisible) {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    showModalSheet.value = false
                }
            }

            UdiBottomSheet(
                bottomSheetState = bottomSheetState,
                sheetContent = {
                    UdiEntryDetails(
                        data = data,
                        modifier = Modifier
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(text = "Borrar",
                                modifier = Modifier.weight(0.5f),
                                shape = RectangleShape,
                                contentColor = colorResource(R.color.error),
                                icon = Icons.Filled.Delete,
                                variant = ButtonVariants.TEXT,
                                onClick = {
                                    hideSheet()
                                    navActions.navigateToDeleteUdiEntry(
                                        R.string.edit_udi,
                                        data.id.toString(),
                                        true
                                    )
                                    //viewModel.deleteUdi(retirementPlan = retirementData.value)
                                })
                            Button(text = "Editar",
                                modifier = Modifier.weight(0.5f),
                                shape = RectangleShape,
                                contentColor = colorResource(R.color.accepted),
                                icon = Icons.Filled.Edit,
                                variant = ButtonVariants.TEXT,
                                onClick = {
                                    hideSheet()
                                    navActions.navigateToAddEditUdiEntry(
                                        R.string.edit_udi,
                                        data.id.toString()
                                    )
                                })
                        }
                    }
                }
            ) {
                UdiHomeScreen(
                    userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                    onUserMessageDisplayed = {
                        entry.arguments?.putInt(
                            USER_MESSAGE_ARG,
                            0
                        )
                    },
                    onAddEntry = { navActions.navigateToAddEditUdiEntry(R.string.add_udi, null) },
                    onEditEntry = {
                        navActions.navigateToAddEditUdiEntry(
                            R.string.edit_udi,
                            it.id.toString()
                        )
                    },
                    onDeleteEntry = {
                        navActions.navigateToDeleteUdiEntry(
                            R.string.edit_udi,
                            it.id.toString(),
                            true
                        )
                    },
                    onUdiClick = { udi ->
                        data = udi
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }
                    },
                    onDetailsClick = {
                        val jsonUdi = Gson().toJson(it)
                        navActions.navigateToUdiGlobalDetails(
                            jsonUdi
                        )
                    }
                )
            }

        }

        composable(
            UdisDestination.ADD_EDIT_TASK_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.IntType },
                navArgument(UDI_ID_ARG) { type = NavType.StringType; nullable = true },
                navArgument(DELETE_ARG) { type = NavType.StringType; nullable = true }
            )
        ) { entry ->
            val udiId = entry.arguments?.getString(UDI_ID_ARG)
            AddRetirementEntryScreen(
                topBarTitle = entry.arguments?.getInt(TITLE_ARG)!!,
                onUdiUpdate = {
                    navActions.navigateToHome(
                        if (udiId == null) ADD_EDIT_RESULT_OK else EDIT_RESULT_OK
                    )
                },
                onDeleteUdi = {
                    navActions.navigateToHome(DELETE_RESULT_OK)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            UdisDestination.ADD_EDIT_COMMISSION_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.IntType },
                navArgument(COMMISSION_ID_ARG) { type = NavType.StringType; nullable = true },
                navArgument(IS_INSERT_COMMISSION) { type = NavType.BoolType}
            )
        ) { entry ->
            val isCommission = entry.arguments?.getBoolean(IS_INSERT_COMMISSION)
            val commissionId = entry.arguments?.getString(UDI_ID_ARG)

            if (isCommission != null) {
                AddRetirementEntryScreen(
                    topBarTitle = entry.arguments?.getInt(TITLE_ARG)!!,
                    isInsertCommission = isCommission,
                    onUdiUpdate = {

                    },
                    onDeleteUdi = {

                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(UdisDestination.UDI_GLOBAL_DETAIL,
            arguments = listOf(
                navArgument(GLOBAL_VALUES) { type = NavType.StringType; }
            )
        ) { entry ->
            val arg = entry.arguments?.getString(GLOBAL_VALUES)?.split("=")?.get(1)
            val obj = Gson().fromJson(arg, UdiGlobalDetails::class.java)
            UdiGlobalDetailsScreen(obj, onBack = { navController.popBackStack() })
        }
    }


//    Scaffold(
//        bottomBar = {
//            BottomNavigation {
//                val navBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentDestination = navBackStackEntry?.destination
//                items.forEach { screen ->
//                    BottomNavigationItem(
//                        icon = { screen.icon?.let { Icon(it, contentDescription = null) } },
//                        label = { Text(text = stringResource(id = screen.resourceId)) },
//                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
//                        onClick = {
//                            navController.navigate(screen.route) {
//                                popUpTo(navController.graph.findStartDestination().id) {
//                                    saveState = true
//                                }
//                                launchSingleTop = true
//                                restoreState = true
//                            }
//                        })
//                }
//            }
//        }
//    ) { innerPadding ->
//        val udiViewModel = hiltViewModel<UdiViewModel>()
//        NavHost(
//            navController = navController,
//            startDestination = startDestination,
//            modifier = Modifier.padding(innerPadding)
//        ) {
//
//            composable(Screen.UdiHomeScreen.route) {
//                UdiHomeScreen(navController, viewModel = udiViewModel)
//            }
//
//            composable(Screen.AddRetirementEntryScreen.route) {
//
//                AddRetirementEntryScreen(navController, viewModel = udiViewModel)
//            }
//
//            composable(Screen.AddRetirementEntryScreen.route + "/{id}") {
//                val id = it.arguments?.getString("id")
//                AddRetirementEntryScreen(
//                    navController = navController,
//                    viewModel = udiViewModel,
//                    retirementPlanId = id?.toLong()
//                )
//            }
//
//            composable(Screen.UdiGlobalDetailsScreen.route) {
//
//                UdiGlobalDetailsScreen(
//                    navController = navController,
//                    viewModel = udiViewModel
//                )
//            }
//        }
//    }

}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3