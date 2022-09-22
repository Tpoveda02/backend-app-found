package com.example.controllers

import com.example.interfaces.InterfaceInvestment
import com.example.models.Investment
import com.example.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.upsert

class ControllInvestment(db: CoroutineDatabase): InterfaceInvestment {

    private val investments = db.getCollection<Investment>()

    //Obtener usuario por nombre
    override suspend fun getInvestmentByName(name: String): Investment? {
        return investments.findOne(Investment::name eq name)
    }
    //Agregar inversión
    override suspend fun insertInvestment(investment: Investment): Boolean {
        return investments.insertOne(investment).wasAcknowledged()
    }
    //Editar inversión
    override suspend fun updateInvestment(investment_user: User, investment: Investment): Boolean {
        return investments.updateOne(Investment::investment eq investment_user, investment, upsert()).wasAcknowledged()
    }
    //Obetener invesiones
    override suspend fun getInvestments(): List<Investment>? {
        return investments.find().toList()
    }
    //Obetener inversionistas
    override suspend fun getInvestmentByInvestment(investment: User): Investment? {
        return investments.findOne(Investment::investment eq investment)
    }

}