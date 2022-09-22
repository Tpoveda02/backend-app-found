package com.example.interfaces
import com.example.models.Investment
import com.example.models.Transaction

interface InterfaceTransaction{
    suspend fun insertTransaction(transaction: Transaction): Boolean
    suspend fun getTransactionInvestments(): List<Transaction>?
}