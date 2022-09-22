package com.example.controllers
import com.example.interfaces.InterfaceUser
import com.example.models.User
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class ControllUser(db: CoroutineDatabase): InterfaceUser {

    private val users = db.getCollection<User>()

    //Obtener usuario por nombre
    override suspend fun getUserByUserName(username: String): User? {
        return users.findOne(User::username eq username)
    }
    //Agregar usuario
    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
    //Editar usuario
    override suspend fun updateUser(username: String, user: User): Boolean {
            return users.updateOne(User::username eq username, user, upsert()).wasAcknowledged()
    }
    //Obetener usuarios
    override suspend fun getUsers(): List<User>? {
        return users.find().toList()
    }

}
