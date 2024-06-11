package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SingleGardenViewModel : ViewModel() {

    private val _isLeaderboardEnabled = MutableStateFlow(true)
    val isLeaderboardEnabled: StateFlow<Boolean> = _isLeaderboardEnabled

    private val _flowerCount = MutableStateFlow(10)
    val flowerCount: StateFlow<Int> = _flowerCount

    fun toggleLeaderboard() {
        _isLeaderboardEnabled.value = !_isLeaderboardEnabled.value
    }

    fun setFlowerCount(count: Int) {
        _flowerCount.value = count
    }
}