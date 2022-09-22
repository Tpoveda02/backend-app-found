package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class User constructor(
    val username: String,
    val password: String,
    val role: String,
    var total_money:Int,
    val state:Int)
