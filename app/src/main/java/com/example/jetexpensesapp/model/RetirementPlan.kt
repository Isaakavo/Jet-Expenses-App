package com.example.jetexpensesapp.model

import java.time.LocalDateTime
import java.util.*

data class RetirementPlan(
    val id: Long = 0,
    val dateOfPurchase: LocalDateTime,
    var purchaseTotal: Double,
    val udiValue: Double,
    val totalOfUdi: Double,
    val mineUdi: Double,
    val udiCommission: Double,
    val udiValueInMoney: Double,
    val udiValueInMoneyCommission: Double
)
