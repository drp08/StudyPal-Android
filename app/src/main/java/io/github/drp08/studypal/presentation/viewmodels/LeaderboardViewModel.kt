package io.github.drp08.studypal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LeaderboardItem(val name: String, val xp: Int)

class LeaderboardViewModel : ViewModel() {

    private val defaultLeaderboardItems = listOf(
        LeaderboardItem("John Doe", 1200),
        LeaderboardItem("Jane Smith", 1100),
        LeaderboardItem("Sam Wilson", 1000),
        LeaderboardItem("Chris Evans", 950),
        LeaderboardItem("Bruce Wayne", 900)
    )

    private val _isLeaderboardEnabled = MutableStateFlow(true)
    var isLeaderboardEnabled: StateFlow<Boolean> = _isLeaderboardEnabled

    private val _isLeaderboard = MutableStateFlow(true)
    var isLeaderboard: StateFlow<Boolean> = _isLeaderboard

    private val _currentlyInLeaderBoardView = MutableStateFlow(true)
    var currentlyInLeaderBoardView: StateFlow<Boolean> = _currentlyInLeaderBoardView

    private val _currentlyInFlowerView = MutableStateFlow(false)
    var currentlyInFlowerView: StateFlow<Boolean> = _currentlyInFlowerView

    private val _leaderboardItems = MutableStateFlow(emptyList<LeaderboardItem>())
    val leaderboardItems: StateFlow<List<LeaderboardItem>> = _leaderboardItems.asStateFlow()

    init {
        viewModelScope.launch {
            _leaderboardItems.value = defaultLeaderboardItems
        }
    }

    fun toggleLeaderboard() {
        _isLeaderboardEnabled.value = !_isLeaderboardEnabled.value
    }

    fun toggleLeaderboardAndFlowers() {
        _isLeaderboard.value = !_isLeaderboard.value
        _currentlyInLeaderBoardView.value = !_currentlyInLeaderBoardView.value
        _currentlyInFlowerView.value = !_currentlyInFlowerView.value
    }

    fun addLeaderboardItem(item: LeaderboardItem) {
        viewModelScope.launch {
            _leaderboardItems.value = leaderboardItems.toMutableList() + item
        }
    }

    private fun <T> StateFlow<List<T>>.toMutableList(): MutableList<T> {
        val currentValue = value
        return currentValue.toMutableList()
    }
}