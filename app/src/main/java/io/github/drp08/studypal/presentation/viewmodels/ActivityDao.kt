package io.github.drp08.studypal.presentation.viewmodels

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek

@Dao
interface ActivityDao {
    @Insert
    suspend fun insertActivity(activity: ActivityEntity)

    @Query("SELECT * FROM activities WHERE dayOfWeek = :dayOfWeek")
    fun getActivitiesForDay(dayOfWeek: DayOfWeek): Flow<List<ActivityEntity>>
}