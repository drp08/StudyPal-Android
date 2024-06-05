package io.github.drp08.studypal.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.drp08.studypal.domain.entities.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Upsert
    suspend fun upsertSubject(subjectEntity: SubjectEntity)

    @Query("SELECT * from subjectentity")
    fun getAllSubjects(): Flow<List<SubjectEntity>>
}