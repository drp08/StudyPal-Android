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

    private val _leaderboardItems = MutableStateFlow(emptyList<LeaderboardItem>())
    val leaderboardItems: StateFlow<List<LeaderboardItem>> = _leaderboardItems.asStateFlow()

    init {
        viewModelScope.launch {
            _leaderboardItems.value = defaultLeaderboardItems
        }
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