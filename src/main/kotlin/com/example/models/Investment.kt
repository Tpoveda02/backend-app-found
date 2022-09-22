package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Investment(val name: String, var investment: User, val min_amount: Int, val state:Int)