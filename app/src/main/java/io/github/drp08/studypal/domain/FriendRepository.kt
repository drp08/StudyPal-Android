package io.github.drp08.studypal.domain

import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    suspend fun getFriends(): List<String>

    suspend fun addNewFriend(friendName: String)

    suspend fun removeFriend(friendName: String)
}