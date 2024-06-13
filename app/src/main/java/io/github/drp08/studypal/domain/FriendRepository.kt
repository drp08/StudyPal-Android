package io.github.drp08.studypal.domain

import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    fun getFriends(): Flow<Result<List<String>>>

    suspend fun addNewFriend(friendName: String): Result<Unit>

    suspend fun removeFriend(friendName: String): Result<Unit>
}