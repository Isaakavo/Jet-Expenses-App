package com.example.jetexpensesapp.navigation

import java.lang.IllegalArgumentException

enum class ExpensesScreens {
    UdiHomeScreen,
    AddRetirementEntryScreen;

    companion object {
        fun fromRoute(route: String?) = when(route?.substringBefore("/")) {
            UdiHomeScreen.name -> UdiHomeScreen
            AddRetirementEntryScreen.name -> AddRetirementEntryScreen
            null -> UdiHomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}