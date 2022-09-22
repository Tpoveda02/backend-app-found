package com.example.models

import kotlinx.serialization.Serializable



@Serializable
data class TransactionClient(val client: User, var type_action: String, val amount:Int, var state:Int)