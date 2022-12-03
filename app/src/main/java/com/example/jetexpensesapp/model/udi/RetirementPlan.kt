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

data class ServerResponse(
    val body: Body = Body(),
    var status: String = "NOT_FETCHED"
)

data class Body(
    val `data`: List<Data> = emptyList(),
    val message: String = "",
    val size: Int = 0,
    val userId: String = ""
)

data class Data(
    val id: Int = 1,
    val retirementRecord: RetirementRecord? = null,
    val udiConversions: UdiConversions? = null
)

data class RetirementRecord(
    val id: Int? = null,
    val purchaseTotal: Double,
    val udiValue: Double,
    val totalOfUdi: Double? = null,
    val dateOfPurchase: String,
    val udiCommission: UdiCommission? = null
)

data class UdiCommission(
    val udiCommssion: Double,
    val userUdis: Double
)

data class UdiCommissionPost(
    val id : Int? = null,
    val userUdis: Double,
    val udiCommission: Double,
    val dateAdded: String
)

data class UdiConversions(
    val udiCommissionConversion: Double,
    val udiConversion: Double
)
