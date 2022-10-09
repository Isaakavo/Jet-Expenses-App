package com.example.jetexpensesapp.data

data class UdiGlobalDetails(
    var totalExpend: Double = 0.0,
    var udisTotal: Double = 0.0,
    var udisConvertion: Double = 0.0,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var paymentDeadLine: Int? = null,
)
