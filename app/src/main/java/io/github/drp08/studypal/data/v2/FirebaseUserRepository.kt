package io.github.drp08.studypal.data.v2

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor() : UserRepository {
    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }
    private val usersRef by lazy { db.collection("users") }

    companion object {
        private const val TAG = "FirebaseUserRepository"
    }

    override fun verifyAndGetUser(): Flow<Result<User>> {
        if (auth.currentUser == null)
            return flowOf(Result.failure(Exception("User not logged in")))

        return flow {
            val uid = auth.currentUser!!.uid
            val user = usersRef.document(uid).get().await().toObject(User::class.java)
            user?.let { emit(Result.success(it)) }
        }
    }

    override fun getUser() = flow {
        val uid = auth.currentUser!!.uid
        val user = usersRef.document(uid).get().await().toObject(User::class.java)!!
        emit(user)
    }

    override suspend fun createUser(email: String, password: String, user: User) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Log.d(TAG, "createUser: successfully created auth user")
            createUserData(user)
        } catch (e: Exception) {
            Log.e(TAG, "createUser: failed creating an auth user", e)
            throw e
        }
    }

    private fun createUserData(user: User) {
        val uid = auth.currentUser!!.uid
        usersRef.document(uid).set(user)
    }
}