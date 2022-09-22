package com.example.routes

import com.example.controllers.ControllUser
import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



fun Route.user(
    userController: ControllUser
) {
    post("/user") {
        var wasAcknowledged = false
        var request = call.receiveOrNull<User>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if(userController.getUserByUserName(request.username)==null){
             wasAcknowledged = userController.insertUser(request)
        }
        if (!wasAcknowledged) {
            call.respondText("El usuario no ha sido creado",status = HttpStatusCode.Conflict)
        }else{
            call.respondText("El usuario ha sido creado", status = HttpStatusCode.Created)
        }
    }
    get("/logIn/{username}/{password}") {
        val username = call.parameters["username"] ?: return@get call.respondText(
            "Nombre invalido",
            status = HttpStatusCode.BadRequest
        )
        val password = call.parameters["password"] ?: return@get call.respondText(
            "Contraseña invalida",
            status = HttpStatusCode.BadRequest
        )
        val user = userController.getUserByUserName(username)
        if (user == null) {
            call.respondText("El usuario no existe",status = HttpStatusCode.Conflict)
        }else{
            if(!password.equals(user.password)) {
                call.respondText("La contraseña no coincide", status = HttpStatusCode.Conflict)
            }else {
                call.respond(user)
            }
        }
    }
    get("/user/{name}") {
        val name = call.parameters["name"] ?: return@get call.respondText(
            "Nombre invalido",
            status = HttpStatusCode.BadRequest
        )
        val user = userController.getUserByUserName(name)
        if (user == null) {
            call.respondText("El usuario no existe",status = HttpStatusCode.Conflict)
        }else{
            call.respond(user)
        }
    }
    get("/user") {
        val users = userController.getUsers()
        if (users == null) {
            call.respondText("No hay usuarios",status = HttpStatusCode.Conflict)
        }else{
            call.respond(users)
        }
    }
    put("/user/{username}"){
        var wasAcknowledged = false
        val username = call.parameters["username"] ?: return@put call.respondText(
            "Id invalido",
            status = HttpStatusCode.BadRequest
        )
        var request = call.receiveOrNull<User>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }
        if(userController.getUserByUserName(request.username)==null){
            wasAcknowledged = userController.updateUser(username,request)
        }

        if (!wasAcknowledged) {
            call.respondText("El usuario no ha sido actualizado",status = HttpStatusCode.Conflict)
        }else{
            call.respondText("El usuario ha sido actualizado", status = HttpStatusCode.Created)
        }
    }
}
