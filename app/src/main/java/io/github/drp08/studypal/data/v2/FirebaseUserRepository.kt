package io.github.drp08.studypal.data.v2

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.domain.models.User
import kotlinx.coroutines.Dispatchers
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

    override suspend fun verifyAndGetUser(): Result<User> {
        if (auth.currentUser == null)
            return Result.failure(Exception("User not logged in"))

        return withContext(Dispatchers.IO) {
            val uid = auth.currentUser!!.uid
            val user = usersRef.document(uid).get().await().toObject(User::class.java)

            withContext(Dispatchers.Default) inner@{
                user?.let { return@inner Result.success(it) }
                    ?: return@inner Result.failure(Exception("User not found"))
            }
        }
    }

    override suspend fun getUser(): User {
        val uid = auth.currentUser!!.uid
        val user = usersRef.document(uid).get().await().toObject(User::class.java)!!
        return user
    }

    override suspend fun createUser(email: String, password: String, user: User) {
        try {
            withContext(Dispatchers.IO) {
                auth.createUserWithEmailAndPassword(email, password).await()
                Log.d(TAG, "createUser: successfully created auth user")
                createUserData(user)
            }
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