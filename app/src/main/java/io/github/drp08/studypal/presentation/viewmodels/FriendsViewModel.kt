package io.github.drp08.studypal.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.drp08.studypal.domain.FriendRepository
import io.github.drp08.studypal.domain.models.v2.Friend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {
    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }

    companion object {
        private const val TAG = "FriendsViewModel"
    }

    var friends by mutableStateOf(emptyList<String>())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            friends = friendRepository.getFriends()
        }
    }

    fun addFriend(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            friendRepository.addNewFriend(name)
        }
    }
}
