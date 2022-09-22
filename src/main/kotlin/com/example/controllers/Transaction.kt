package com.example.controllers

import com.example.interfaces.InterfaceTransaction
import com.example.models.Investment
import com.example.models.Transaction
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ControllTransaction(db: CoroutineDatabase): InterfaceTransaction{

    private val transactions = db.getCollection<Transaction>()

    //Agregar dinero
    override suspend fun insertTransaction(transaction: Transaction): Boolean {
        return transactions.insertOne(transaction).wasAcknowledged()
    }
    //Obtener tranasacciones por inversor
    override suspend fun getTransactionInvestments(): List<Transaction> {
        return transactions.find().toList()
    }
}