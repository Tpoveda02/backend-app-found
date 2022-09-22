package com.example.controllers

import com.example.interfaces.InterfaceTransaction
import com.example.interfaces.InterfaceTransactionClient
import com.example.models.Investment
import com.example.models.Transaction
import com.example.models.TransactionClient
import com.example.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ControllTransactionClient(db: CoroutineDatabase): InterfaceTransactionClient{

    private val transactionsClients = db.getCollection<TransactionClient>()

    //Agregar dinero
    override suspend fun insertTransactionClient(transactionClient: TransactionClient): Boolean {
        return transactionsClients.insertOne(transactionClient).wasAcknowledged()
    }
    //Obtener transacciones por cliente
    override suspend fun getTransactionClients(): List<TransactionClient>? {
        return transactionsClients.find().toList()
    }


}