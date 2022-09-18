package com.example.jetexpensesapp.model

import com.google.gson.annotations.SerializedName

data class UdiItem(
    @SerializedName("dato")
    val udiValue: String,
    @SerializedName("fecha")
    val date: String
)