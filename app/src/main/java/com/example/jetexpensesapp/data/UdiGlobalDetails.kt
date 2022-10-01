package com.example.jetexpensesapp.data

data class UdiGlobalDetails(
    var totalExpend: Double,
    var udisTotal: Double,
    var udisConvertion: Double,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var paymentDeadLine: Int? = null,
)
