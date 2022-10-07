package com.example.jetexpensesapp.navigation

import android.app.Activity
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
import com.example.jetexpensesapp.model.RetirementPlan
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.DELETE_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.TITLE_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.UDI_ID_ARG
import com.example.jetexpensesapp.navigation.UdiDestinationArgs.USER_MESSAGE_ARG
import com.example.jetexpensesapp.screen.udis.AddRetirementEntryScreen
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
    startDestination: String = UdisDestination.UDI_HOMESCREEN_ROUTE,
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
            UdisDestination.UDI_HOMESCREEN_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) { type = NavType.IntType; defaultValue = 0 }
            )
        ) { entry ->

            var retirementDataBottomSheet by remember { mutableStateOf(RetirementPlan()) }

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
                        retirementPlan = retirementDataBottomSheet,
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
                                        retirementDataBottomSheet.id.toString(),
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
                                        retirementDataBottomSheet.id.toString()
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
                        retirementDataBottomSheet = udi
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }
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