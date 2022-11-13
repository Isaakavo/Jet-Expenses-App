package com.example.jetexpensesapp.model.udi

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "udis")
data class RetirementPlan(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "purchase_date")
    val dateOfPurchase: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "purchase_total")
    var purchaseTotal: Double = 0.0,
    @ColumnInfo(name = "udi_value")
    val udiValue: Double = 0.0,
    @ColumnInfo(name = "udi_total")
    val totalOfUdi: Double = 0.0,
    @ColumnInfo(name = "mine_udi")
    val mineUdi: Double = 0.0,
    @ColumnInfo(name = "udi_commission")
    val udiCommission: Double = 0.0,
    @ColumnInfo(name = "udi_money")
    val udiValueInMoney: Double = 0.0,
    @ColumnInfo(name = "commission_money")
    val udiValueInMoneyCommission: Double = 0.0
)
