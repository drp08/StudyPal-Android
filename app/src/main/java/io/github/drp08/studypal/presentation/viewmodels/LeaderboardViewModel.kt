package io.github.drp08.studypal.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.firestore
import io.github.drp08.studypal.data.v2.FirebaseUserRepository
import io.github.drp08.studypal.domain.models.User
import io.github.drp08.studypal.domain.models.v2.Friend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LeaderboardItem(val name: String, val xp: Int)

class LeaderboardViewModel : ViewModel() {
    companion object {
        private const val TAG = "LeaderboardViewModel"
    }
    private val db by lazy { Firebase.firestore }
    private val auth by lazy { Firebase.auth }
    private val userRepository by lazy{FirebaseUserRepository}

    private val defaultLeaderboardItems = listOf(
        LeaderboardItem("Test",0)
//        LeaderboardItem("John Doe", 1200),
//        LeaderboardItem("Jane Smith", 1100),
//        LeaderboardItem("Sam Wilson", 1000),
//        LeaderboardItem("Chris Evans", 950),
//        LeaderboardItem("Bruce Wayne", 900)
    )
//    var friends by mutableStateOf(emptyList<>())

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
            db.collection("friends")
                .where(
                    Filter.or(
                        Filter.equalTo("friend1", auth.currentUser!!.uid),
                        Filter.equalTo("friend2", auth.currentUser!!.uid)
                    )
                )
                .addSnapshotListener { snapshot, error ->
                    error?.let {
                        Log.e(LeaderboardViewModel.TAG, "error listening to friends: ${it.message}", it)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val friendObjs = snapshot.toObjects(Friend::class.java)
                        getLeaderboardItems(friendObjs)
                    }
                }
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

    fun getLeaderboardItems(friends: List<Friend>) {
        val friendsUid = friends.map {
            if (it.friend1 == auth.currentUser!!.uid)
                it.friend2
            else
                it.friend1
        }
        for (friendUid in friendsUid) {
            db.collection("users")
                .document(friendUid)
                .get()
                .addOnSuccessListener { task ->
                    task.toObject(User::class.java)?.let {
                        addLeaderboardItem(LeaderboardItem(it.name, it.xp))
                    }
                }
        }
    }
    private fun <T> StateFlow<List<T>>.toMutableList(): MutableList<T> {
        val currentValue = value
        return currentValue.toMutableList()
    }
}