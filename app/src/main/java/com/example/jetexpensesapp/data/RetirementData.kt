package com.example.jetexpensesapp.data

import com.example.jetexpensesapp.model.RetirementPlan
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class RetirementData {
    fun load(): ArrayList<RetirementPlan> {
        return arrayListOf(
            RetirementPlan(
                1,
                LocalDateTime.now(),
                3453.05,
                7.45,
                463.21,
                437.12,
                26.09,
                3258.53,
                194.52
            ),
            RetirementPlan(
                2,
                LocalDateTime.now(),
                3474.27,
                7.501214,
                463.16,
                437.12,
                26.04,
                3278.93,
                195.34
            )
        )
    }
}