package io.github.drp08.studypal.data

import io.github.drp08.studypal.domain.FriendRepository
import io.github.drp08.studypal.domain.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val userRepository: UserRepository
) : FriendRepository {
    override fun getFriends(): Flow<Result<List<String>>> = flow {
        val user = userRepository.getUserLocal().first()
        val response = client.get("/database/friends/${user.name}")
        if (!response.status.isSuccess())
            emit(Result.failure(Exception("Failed to retrieve list of friends")))
        else
            emit(Result.success(response.body<List<String>>()))
    }

    override suspend fun addNewFriend(friendName: String): Result<Unit> {
        val user = userRepository.getUserLocal().first()
        val response = client.post("/database/friends") {
            setBody("${user.name} $friendName")
        }
        if (!response.status.isSuccess())
            return Result.failure(Exception("Encountered an error while trying to add new friend"))
        return Result.success(Unit)
    }

    override suspend fun removeFriend(friendName: String): Result<Unit> {
        val user = userRepository.getUserLocal().first()
        val response = client.delete("/database/friends") {
            setBody("${user.name} $friendName")
        }
        if (!response.status.isSuccess())
            return Result.failure(Exception("Encountered an error while trying to remove a friend"))
        return Result.success(Unit)
    }
}