package io.github.drp08.studypal.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.drp08.studypal.domain.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(userEntity: UserEntity)

    @Query("SELECT * from user")
    fun getUser(): Flow<UserEntity>
}