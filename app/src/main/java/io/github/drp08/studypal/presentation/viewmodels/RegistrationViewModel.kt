package io.github.drp08.studypal.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.session.Session
import io.github.drp08.studypal.domain.models.User
import io.github.drp08.studypal.presentation.screens.HomeScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val session: Session
) : ViewModel() {
    var user by mutableStateOf(
        User(
            name = "",
            startWorkingHours = 7 * 60 * 60 * 1000L,
            endWorkingHours = 16 * 60 * 60 * 1000L,
            maxStudyingHours = 6
        )
    )
        private set

    fun onNameChange(newName: String) {
        user = user.copy(name = newName)
    }

    fun onWorkingHoursStartChange(newValue: Long) {
        user = user.copy(startWorkingHours = newValue - 1 * 60 * 60 * 1000)
    }

    fun onWorkingHoursEndChange(newValue: Long) {
        user = user.copy(endWorkingHours = newValue - 1 * 60 * 60 * 1000)
    }

    fun onHoursPerDayChange(newValue: Int) {
        user = user.copy(maxStudyingHours = newValue)
    }

    fun onRegister(navigator: Navigator) {
        if (user.name.isBlank())
            return

        viewModelScope.launch {
            session.setUser(user)

            navigator.push(HomeScreen)
        }
    }

    fun isUserRegistered(): Boolean {
        return runBlocking {
            session.getCurrentUser().first() != null
        }
    }
}