package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val name: String,
    val startWorkingHours: Long, // Epoch millis
    val endWorkingHours: Long, // Epoch millis
    val maxStudyingHours: Int
){
    fun toSerializable() : User {
        return User(
            name,
            startWorkingHours,
            endWorkingHours,
            maxStudyingHours
        )
    }
    companion object {
        fun fromSerializable(user: User): UserEntity {
            return UserEntity(
                user.name,
                user.startWorkingHours,
                user.endWorkingHours,
                user.maxStudyingHours
            )
        }
    }
}