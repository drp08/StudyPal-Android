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
    private val db by lazy { Firebase.firestore }
    private val auth by lazy {Firebase.auth}
    companion object {
        private const val TAG = "FriendsViewModel"
    }

    var friends by mutableStateOf(emptyList<String>())
        private set

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
                        Log.e(TAG, "error listening to friends: ${it.message}", it)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val friendObjs = snapshot.toObjects(Friend::class.java)
                        friends = friendObjs.map {
                            if (it.friend1 == auth.currentUser!!.uid)
                                it.friend2
                            else it.friend1
                        }
                    }
                    }
        }
    }

    fun addFriend(name: String) {
        viewModelScope.launch {
            Log.d(TAG, "addFriend($name) called ")
            friendRepository.addNewFriend(name)
        }
    }
}
