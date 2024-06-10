package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.session.UserSession
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val userSession: UserSession
): ViewModel() {
    fun isUserRegistered(): Boolean {
        return runBlocking {
            userSession.getCurrentUser().first() != null
        }
    }
}
