package com.example.interfaces

import com.example.models.User


interface InterfaceUser{
    suspend fun getUserByUserName(username: String): User?
    suspend fun insertUser(user: User): Boolean
    suspend fun updateUser(id:String, user: User): Boolean
    suspend fun getUsers(): List<User>?
}