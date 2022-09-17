package com.example.jetexpensesapp.model

import java.util.*

data class RetirementPlan(
    val id: Long,
    val dateOfPurchase: Date,
    val purchaseTotal: Double,
    val udiValue: Double,
    val totalOfUdi: Double,
    val mineUdi: Double,
    val udiCommission: Double,
    val udiValueInMoney: Double,
    val udiValueInMoneyCommission: Double
)
