package io.github.drp08.studypal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.domain.entities.SessionEntity

@Database(
    entities = [SessionEntity::class],
    version = 1
)
abstract class SessionDatabase : RoomDatabase() {
    abstract val sessionDao: SessionDao
}