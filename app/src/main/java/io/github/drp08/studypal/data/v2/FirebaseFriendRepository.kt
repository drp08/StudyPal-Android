package io.github.drp08.studypal.data.v2

import io.github.drp08.studypal.domain.FriendRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseFriendRepository @Inject constructor(): FriendRepository {
    override fun getFriends(): Flow<Result<List<String>>> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewFriend(friendName: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFriend(friendName: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}