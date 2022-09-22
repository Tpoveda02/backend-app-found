package com.example.routes
import com.example.controllers.ControllInvestment
import com.example.controllers.ControllUser
import com.example.models.Investment
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.investment(
    investmentController: ControllInvestment,
    userController: ControllUser
) {
    post("/investment") {
        var wasAcknowledged = false
        var request = call.receiveOrNull<Investment>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if (investmentController.getInvestmentByInvestment(request.investment) == null) {
            wasAcknowledged = investmentController.insertInvestment(request)
        }
        if (!wasAcknowledged) {
            call.respondText("La inversión no ha sido creada", status = HttpStatusCode.Conflict)
        } else {
            call.respondText("La inversión ha sido creada", status = HttpStatusCode.Created)
        }
    }
    get("/investment/{name}") {
        val name = call.parameters["name"] ?: return@get call.respondText(
            "Nombre invalido",
            status = HttpStatusCode.BadRequest
        )
        val user_investment = userController.getUserByUserName(name)!!
        val investment = investmentController.getInvestmentByInvestment(user_investment)
        if (investment == null) {
            call.respondText("La inversión no existe", status = HttpStatusCode.Conflict)
        } else {
            call.respond(investment)
        }
    }
    get("/investment") {
        val investments = investmentController.getInvestments()
        if (investments == null) {
            call.respondText("No hay no inversiones", status = HttpStatusCode.Conflict)
        } else {
            call.respond(investments)
        }
    }
    put("/investment/{name}/investment_user/{username}") {
        var flag = false
        val name = call.parameters["name"] ?: return@put call.respondText(
            "Nombre invalido",
            status = HttpStatusCode.BadRequest
        )
        val username = call.parameters["username"] ?: return@put call.respondText(
            "Nombre invalido",
            status = HttpStatusCode.BadRequest
        )

        var request = call.receiveOrNull<Investment>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }
        val investment_user = userController.getUserByUserName(username)
        val investment = investmentController.getInvestmentByName(request.name);
        if (investment != null) {
            if (investmentController.getInvestmentByInvestment(request.investment) == null || request.investment == investment_user) {
                flag = investmentController.updateInvestment(request.investment, request)
            }
        }
        if (!flag) {
            call.respondText("La inversión no ha sido actualizada", status = HttpStatusCode.Conflict)
        } else {
            call.respondText("La inversión ha sido actualizada", status = HttpStatusCode.Created)
        }
    }
}

