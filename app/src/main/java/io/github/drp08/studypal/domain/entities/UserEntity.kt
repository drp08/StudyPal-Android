package io.github.drp08.studypal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.drp08.studypal.domain.models.User

@Entity
data class UserEntity(
    @PrimaryKey
    val name: String,
    val startWorkingHours: Int, // Second of the day?
    val endWorkingHours: Int, // Second of the day?
    val maxNumberOfStudyHours: Int
){
    fun toSerializable() : User {
        return User(
            name,
            startWorkingHours,
            endWorkingHours,
            maxNumberOfStudyHours
        )
    }
    companion object {
        fun fromSerializable(user: User): UserEntity {
            return UserEntity(
                user.name,
                user.startWorkingHours,
                user.endWorkingHours,
                user.maxStudyHours
            )
        }
    }
}