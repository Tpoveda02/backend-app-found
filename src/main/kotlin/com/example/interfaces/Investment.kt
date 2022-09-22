package com.example.interfaces

import com.example.models.Investment
import com.example.models.User

interface InterfaceInvestment{
    suspend fun getInvestmentByName(name: String): Investment?
    suspend fun insertInvestment(investment: Investment): Boolean
    suspend fun updateInvestment(investment_user: User, investment: Investment): Boolean
    suspend fun getInvestments(): List<Investment>?
    suspend fun getInvestmentByInvestment(investment: User):Investment?
}