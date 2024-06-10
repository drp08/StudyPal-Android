package io.github.drp08.studypal.db.session

import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.drp08.studypal.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSession @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        const val USER = "user"

        val ActiveUser = compositionLocalOf<User> { error("No active user was found!") }
    }

    private val name = stringPreferencesKey("USER_NAME")
    private val maxStudyingHours = intPreferencesKey("USER_MAX_STUDYING_HOURS")
    private val startWorkingHours = longPreferencesKey("USER_START_WORKING_HOURS")
    private val endWorkingHours = longPreferencesKey("USER_END_WORKING_HOURS")

    fun getCurrentUser(): Flow<User?> =
        dataStore.data.catch {
            emit(emptyPreferences())
        }.map {
            val name = it[name] ?: return@map null
            val maxStudyingHours = it[maxStudyingHours] ?: return@map null
            val startWorkingHours = it[startWorkingHours] ?: return@map null
            val endWorkingHours = it[endWorkingHours] ?: return@map null

            User(
                name = name,
                startWorkingHours = startWorkingHours,
                endWorkingHours = endWorkingHours,
                maxStudyingHours = maxStudyingHours
            )
        }

    suspend fun setUser(user: User) {
        dataStore.edit {
            it[name] = user.name
            it[maxStudyingHours] = user.maxStudyingHours
            it[startWorkingHours] = user.startWorkingHours
            it[endWorkingHours] = user.endWorkingHours
        }
    }
}