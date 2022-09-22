package com.example

import com.example.controllers.ControllInvestment
import com.example.controllers.ControllTransaction
import com.example.controllers.ControllTransactionClient
import com.example.controllers.ControllUser
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "FOUND"
    val db = KMongo.createClient("mongodb+srv://Tpoveda:Nitazha123.@clustertp.8rqa9vs.mongodb.net/?retryWrites=true&w=majority").coroutine.getDatabase(dbName)
    val controllerUser = ControllUser(db)
    val controllerInvestment = ControllInvestment(db)
    val controllerTransaction = ControllTransaction(db)
    val controllTransactionClient = ControllTransactionClient(db)
    configureRouting(controllerUser,controllerInvestment,controllerTransaction,controllTransactionClient)
    configureSerialization()
    configureMonitoring()
    configureSecurity()
}