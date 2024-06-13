package io.github.drp08.studypal.domain

import io.github.drp08.studypal.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun verifyAndGetUser(): Flow<Result<User>>
    fun getUser(): Flow<User>
    suspend fun createUser(email: String, password: String, user: User)
}