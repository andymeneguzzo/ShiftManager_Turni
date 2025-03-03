package com.andy.shiftmanager

data class Shift(
    val id: Long = 0,
    val dataOra: String,
    val oreLavorate: Double,
    val pagaOraria: Double,
    val note: String
)