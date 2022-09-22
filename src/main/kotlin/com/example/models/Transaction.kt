package com.example.models

import kotlinx.serialization.Serializable



@Serializable
data class Transaction(val client: User, val investment: Investment, var type_action: String, val amount:Int, var state:Int)