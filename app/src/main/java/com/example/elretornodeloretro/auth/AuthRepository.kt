package com.example.elretornodeloretro.auth

interface AuthRepository {
    suspend fun signUp(username: String, password: String):AuthResult<Unit>
    suspend fun signIn(username: String, password: String):AuthResult<Unit>
    suspend fun authenticate():AuthResult<Unit>
}