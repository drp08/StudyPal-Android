package io.github.drp08.studypal.domain

import io.github.drp08.studypal.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun verifyAndGetUser(): Result<User>
    suspend fun getUser(): User
    suspend fun createUser(email: String, password: String, user: User): Boolean
    suspend fun loginUser(email: String, password: String)
}