package io.github.drp08.studypal.data

import io.github.drp08.studypal.domain.FriendRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val userRepository: Unit /* UserRepository */
): FriendRepository {
    override fun getFriends(): Flow<Result<List<String>>> {
        TODO("Require UserRepository from feat/register-user-on-server")
    }

    override suspend fun addNewFriend(friendName: String): Result<Unit> {
        TODO("Require UserRepository from feat/register-user-on-server")
    }

    override suspend fun removeFriend(friendName: String): Result<Unit> {
        TODO("Require UserRepository from feat/register-user-on-server")
    }
}