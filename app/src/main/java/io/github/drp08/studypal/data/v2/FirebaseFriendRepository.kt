package io.github.drp08.studypal.data.v2

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.github.drp08.studypal.domain.FriendRepository
import io.github.drp08.studypal.domain.models.v2.Friend
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFriendRepository @Inject constructor() : FriendRepository {
    private val db by lazy { Firebase.firestore }
    private val auth by lazy { Firebase.auth }

    override suspend fun getFriends(): List<String> {
        val snapshot = db.collection("friends")
            .where(
                Filter.or(
                    Filter.equalTo("friend1", auth.currentUser!!.uid),
                    Filter.equalTo("friend2", auth.currentUser!!.uid)
                )
            )
            .get()
            .await()

        if (snapshot != null) {
            val friendObjs = snapshot.toObjects(Friend::class.java)
            return friendObjs.map {
                if (it.friend1 == auth.currentUser!!.uid) it.friend2 else it.friend1
            }
        }
        return emptyList()
    }

    override suspend fun addNewFriend(friendName: String) {
        val queryResults = db.collection("users")
            .whereEqualTo("name", friendName)
            .get()
            .await()
            .documents

        if (queryResults.isEmpty())
            return

        val friendUid = queryResults[0].id
        db.collection("friends")
            .add(Friend(auth.currentUser!!.uid, friendUid))
    }

    override suspend fun removeFriend(friendName: String) {
        db.collection("friends")
            .where(
                Filter.or(
                    Filter.equalTo("friend1", friendName),
                    Filter.equalTo("friend2", friendName)
                )
            )
            .get()
            .await()
            .documents
            .forEach { doc ->
                db.collection("friends")
                    .document(doc.id)
                    .delete()
            }
    }
}