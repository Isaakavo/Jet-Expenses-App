package com.example.jetexpensesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "udis")
data class RetirementPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "purchase_date")
    val dateOfPurchase: LocalDateTime,
    @ColumnInfo(name = "purchase_total")
    var purchaseTotal: Double,
    @ColumnInfo(name = "udi_value")
    val udiValue: Double,
    @ColumnInfo(name = "udi_total")
    val totalOfUdi: Double,
    @ColumnInfo(name = "mine_udi")
    val mineUdi: Double,
    @ColumnInfo(name = "udi_commission")
    val udiCommission: Double,
    @ColumnInfo(name = "udi_money")
    val udiValueInMoney: Double,
    @ColumnInfo(name = "commission_money")
    val udiValueInMoneyCommission: Double
)
