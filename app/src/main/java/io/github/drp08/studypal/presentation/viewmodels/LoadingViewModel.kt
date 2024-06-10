package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.session.Session
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val session: Session
): ViewModel() {
    fun isUserRegistered(): Boolean {
        return runBlocking {
            session.getCurrentUser().first() != null
        }
    }
}
