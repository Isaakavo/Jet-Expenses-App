package com.example.jetexpensesapp.data

data class GlobalServerResponse(
    val body: GlobalBody = GlobalBody(),
    var status: String = "NOT_FETCHED"
)

data class GlobalBody(
    val `data`: List<UdiGlobalDetails> = emptyList(),
    val message: String = "",
    val size: Int = 0,
    val userId: String = ""
)

data class UdiGlobalDetails(
    var totalExpend: Double = 0.0,
    var udisTotal: Double = 0.0,
    var udisConvertion: Double = 0.0,
    var rendimiento: Double = 0.0,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var paymentDeadLine: Int? = null,
    var udiValueToday: String = ""
)
