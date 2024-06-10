package io.github.drp08.studypal.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.db.session.Session
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val session: Session
): ViewModel() {
    companion object {
        private const val TAG = "ProfileViewModel"
    }


    init {
        viewModelScope.launch {
            session.getCurrentUser().collectLatest {
                Log.d(TAG, "userEntity: $it")
            }
        }
    }
}