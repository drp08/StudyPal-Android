package io.github.drp08.studypal.data.v2

import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.domain.models.User
import kotlinx.coroutines.flow.Flow

class FirebaseUserRepository : UserRepository {
    override fun verifyAndGetUser(): Flow<Result<User>> {
        TODO("Not yet implemented")
    }

    override fun getUser(): Flow<User> {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(user: User) {
        TODO("Not yet implemented")
    }
}