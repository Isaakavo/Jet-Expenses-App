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

data class ResponseRetirementRecord(
    val retirementRecord: RetirementRecord? = null,
    val udiCommission: UdiCommission? = null,
    val udiConversions: UdiConversions? = null
)

data class ServerResponse(
    val body: Body,
    val status: String
)

data class Body(
    val `data`: List<Data>,
    val message: String,
    val size: Int,
    val userId: String
)

data class Data(
    val retirementRecord: RetirementRecord,
    val udiCommission: UdiCommission,
    val udiConversions: UdiConversions
)

data class RetirementRecord(
    val dateOfPurchase: String,
    val id: Int,
    val purchaseTotal: Double,
    val udiValue: Double
)

data class UdiCommission(
    val udiCommssion: Double,
    val userUdis: Double
)

data class UdiConversions(
    val udiCommissionConversion: Double,
    val udiConversion: Double
)
