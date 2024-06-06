package io.github.drp08.studypal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.db.daos.UserDao
import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity
import io.github.drp08.studypal.domain.entities.UserEntity

@Database(
    entities = [SessionEntity::class, UserEntity::class, TopicEntity::class, SubjectEntity:: class],
    version = 3
)
abstract class ClientDatabase : RoomDatabase() {
    abstract val sessionDao: SessionDao
    abstract val userDao: UserDao
    abstract val topicDao: TopicDao
    abstract val subjectDao: SubjectDao
}