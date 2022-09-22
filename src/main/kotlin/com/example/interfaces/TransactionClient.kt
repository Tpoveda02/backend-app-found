package com.example.interfaces

import com.example.models.TransactionClient
import com.example.models.User

interface InterfaceTransactionClient {
    suspend fun insertTransactionClient(transactionClient: TransactionClient): Boolean
    suspend fun getTransactionClients(): List<TransactionClient>?
}
