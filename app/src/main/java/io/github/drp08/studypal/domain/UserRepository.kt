package io.github.drp08.studypal.domain

import io.github.drp08.studypal.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun createUser(user: User)
    suspend fun verifyUser()
}