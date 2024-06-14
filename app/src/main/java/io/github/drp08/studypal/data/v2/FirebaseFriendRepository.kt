package io.github.drp08.studypal.data.v2

import io.github.drp08.studypal.domain.FriendRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseFriendRepository @Inject constructor(): FriendRepository {
    override fun getFriends(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewFriend(friendName: String) {

    }

    override suspend fun removeFriend(friendName: String) {
        TODO("Not yet implemented")
    }
}