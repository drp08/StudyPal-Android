package io.github.drp08.studypal.db.session

import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.domain.models.User
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSession @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val client: HttpClient
) : UserRepository {
    companion object {
        const val USER = "user"

        val ActiveUser = compositionLocalOf<User> { error("No active user was found!") }
    }

    private val name = stringPreferencesKey("USER_NAME")
    private val maxStudyingHours = intPreferencesKey("USER_MAX_STUDYING_HOURS")
    private val startWorkingHours = longPreferencesKey("USER_START_WORKING_HOURS")
    private val endWorkingHours = longPreferencesKey("USER_END_WORKING_HOURS")

    override fun verifyAndGetUser(): Flow<Result<User>> {
        return getUser()
            .catch { Result.failure<User>(it) }
            .map { Result.success(it) }
    }

    override fun getUser(): Flow<User> =
        dataStore.data.catch {
            emit(emptyPreferences())
        }.map {
            val name = it[name]
                ?: throw Exception("User's name was not found")
            val maxStudyingHours = it[maxStudyingHours]
                ?: throw Exception("User's maxStudyingHours was not found")
            val startWorkingHours = it[startWorkingHours]
                ?: throw Exception("User's startWorkingHours was not found")
            val endWorkingHours = it[endWorkingHours]
                ?: throw Exception("User's endWorkingHours was not found")

            User(
                name = name,
                startWorkingHours = startWorkingHours,
                endWorkingHours = endWorkingHours,
                maxStudyingHours = maxStudyingHours
            )
        }

    override suspend fun createUser(user: User) {
        dataStore.edit {
            it[name] = user.name
            it[maxStudyingHours] = user.maxStudyingHours
            it[startWorkingHours] = user.startWorkingHours
            it[endWorkingHours] = user.endWorkingHours
        }
    }

    private fun <T> Result.Companion.failure(message: String) = failure<T>(Exception(message))
}