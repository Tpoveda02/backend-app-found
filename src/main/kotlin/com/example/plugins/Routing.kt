package com.example.plugins

import com.example.controllers.ControllInvestment
import com.example.controllers.ControllTransaction
import com.example.controllers.ControllTransactionClient

import com.example.controllers.ControllUser
import com.example.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(userController: ControllUser,
                                 investmentControll: ControllInvestment,
                                 transactionControll: ControllTransaction,
                                 transactionClientControll:ControllTransactionClient
) {
    routing {
        user(userController)
        investment(investmentControll, userController)
        transaction(transactionControll,transactionClientControll,userController,investmentControll)
    }
}
