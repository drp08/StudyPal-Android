package io.github.drp08.studypal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import io.github.drp08.studypal.domain.entities.SessionEntity
import io.github.drp08.studypal.domain.entities.SubjectEntity
import io.github.drp08.studypal.domain.entities.TopicEntity

@Database(
    entities = [SessionEntity::class, TopicEntity::class, SubjectEntity:: class],
    version = 5
)
abstract class ClientDatabase : RoomDatabase() {
    abstract val sessionDao: SessionDao
    abstract val topicDao: TopicDao
    abstract val subjectDao: SubjectDao
}