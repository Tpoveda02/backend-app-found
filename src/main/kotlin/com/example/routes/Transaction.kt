package com.example.routes
import com.example.controllers.ControllInvestment
import com.example.controllers.ControllTransaction
import com.example.controllers.ControllTransactionClient
import com.example.controllers.ControllUser
import com.example.models.Transaction
import com.example.models.TransactionClient
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.transaction(
    transactionController: ControllTransaction,
    transactionClientController:ControllTransactionClient,
    userController: ControllUser,
    investmentController: ControllInvestment
) {
    post("/transaction/client/add") {
        var wasAcknowledged = false
        var request = call.receiveOrNull<TransactionClient>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        request.type_action = "Agregar"
        var user = userController.getUserByUserName(request.client.username)
        request.state = 1
        if(user != null){
                user.total_money += request.amount
                wasAcknowledged = transactionClientController.insertTransactionClient(request)
                userController.updateUser(user.username,user)
        }
        if (!wasAcknowledged) {
            call.respondText("La inversión no ha sido creada",status = HttpStatusCode.Conflict)
        }else{
            call.respondText("La inversión ha sido creada", status = HttpStatusCode.Created)
        }
    }
    post("/transaction/investment/add") {
        var wasAcknowledged = false
        var request = call.receiveOrNull<Transaction>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        request.type_action = "Agregar"
        var user = userController.getUserByUserName(request.client.username)
        var investment = investmentController.getInvestmentByInvestment(request.investment.investment)
        request.state = 1
        if(user != null){
            if (investment != null) {
                if(user.total_money < request.amount){
                    call.respondText("La inversión no ha sido creada, no cuenta con suficiente dinero",status = HttpStatusCode.Conflict)
                }else{
                    if(investment.min_amount<=request.amount){
                        investment.investment.total_money += request.amount
                        wasAcknowledged = transactionController.insertTransaction(request)
                        investmentController.updateInvestment(investment.investment,investment)
                    }else{
                        call.respondText("La inversión no ha sido creada, el dinero mínimo para invertir es $${investment.min_amount}",status = HttpStatusCode.Conflict)
                    }
                }
            }
        }
        if (!wasAcknowledged) {
            call.respondText("La inversión no ha sido creada",status = HttpStatusCode.Conflict)
        }else{
            call.respondText("La inversión ha sido creada", status = HttpStatusCode.Created)
        }
    }
    post("/transaction/client/remove") {
        var wasAcknowledged = false
        var request = call.receiveOrNull<TransactionClient>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        request.type_action = "Retirar"
        var user = userController.getUserByUserName(request.client.username)
        request.state = 1
        if(user != null){
            if(request.amount<=user.total_money) {
                user.total_money -= request.amount
                wasAcknowledged = transactionClientController.insertTransactionClient(request)
                userController.updateUser(user.username,user)
            }else{
                call.respondText("No cuenta con suficiente dinero para retirar",status = HttpStatusCode.Conflict)
            }
        }
        if (!wasAcknowledged) {
            call.respondText("La inversión no ha sido creada",status = HttpStatusCode.Conflict)
        }else{
            call.respondText("La inversión ha sido creada", status = HttpStatusCode.Created)
        }
    }
    post("/transaction/investment/remove") {
        var wasAcknowledged = false
        var request = call.receiveOrNull<Transaction>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        request.type_action = "Retirar"
        var user = userController.getUserByUserName(request.client.username)
        var investment = investmentController.getInvestmentByInvestment(request.investment.investment)
        request.state = 1
        if(user != null){
            if (investment != null) {
                if(user.total_money < request.amount){
                    call.respondText("No cuenta con suficiente dinero para retirar de la inversión",status = HttpStatusCode.Conflict)
                }else{
                    if(investment.min_amount<=investment.investment.total_money-request.amount || investment.investment.total_money-request.amount==0){
                        investment.investment.total_money -= request.amount
                        wasAcknowledged = transactionController.insertTransaction(request)
                        investmentController.updateInvestment(investment.investment,investment)
                    }else{
                        call.respondText("El retiro no se ha hecho, ya que el dinero mínimo para invertir es $${investment.min_amount}",status = HttpStatusCode.Conflict)
                    }
                }
            }
        }
        if (!wasAcknowledged) {
            call.respondText("La inversión no ha sido creada",status = HttpStatusCode.Conflict)
        }else{
            call.respondText("La inversión ha sido creada", status = HttpStatusCode.Created)
        }
    }
    get("/transaction/client/{username}") {
        val username = call.parameters["username"] ?: return@get call.respondText(
            "Nombre invalido",
            status = HttpStatusCode.BadRequest
        )
        var client = userController.getUserByUserName(username)
        var transaction_client:List<TransactionClient>
        var transaction_investment:List<Transaction>
        if(client!=null){
            transaction_client = transactionClientController.getTransactionClients()!!.filter{ it.client.username == username}
            transaction_investment = transactionController.getTransactionInvestments().filter { it.client.username == username}
            val transactions = transaction_client.plus(transaction_investment)
            if (transactions == null) {
                call.respondText("No hay transacciones para este cle",status = HttpStatusCode.Conflict)
            }else{
                call.respond(transactions)
            }
        }else{
            call.respondText("El cliente no existe",status = HttpStatusCode.Conflict)
        }

    }
    get("/transaction/investment/{username}") {
        val username = call.parameters["username"] ?: return@get call.respondText(
            "Nombre invalido",
            status = HttpStatusCode.BadRequest
        )
        var investment_user = userController.getUserByUserName(username)
        var transaction_investment:List<Transaction>
        if(investment_user!=null){
            transaction_investment = transactionController.getTransactionInvestments().filter { it.investment.investment.username == username}
            if (transaction_investment == null) {
                call.respondText("No hay transacciones para esta inversión",status = HttpStatusCode.Conflict)
            }else{
                call.respond(transaction_investment)
            }
        }else{
            call.respondText("La inversión no existe",status = HttpStatusCode.Conflict)
        }
    }
    get("/transactions"){
        val transactions = transactionController.getTransactionInvestments()
        if (transactions == null) {
            call.respondText("No hay transacciones",status = HttpStatusCode.Conflict)
        }else{
            call.respond(transactions)
        }
    }
    get("/client/transactions"){
        val clientTransactions = transactionClientController.getTransactionClients()
        if (clientTransactions == null) {
            call.respondText("No hay transacciones",status = HttpStatusCode.Conflict)
        }else{
            call.respond(clientTransactions)
        }
    }

}