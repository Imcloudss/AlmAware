package com.example.almaware.data.repository

import com.example.almaware.data.model.User
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val db: DatabaseReference
) {
    private val usersRef = db.child("users")

    suspend fun getUser(uid: String): User? {
        val snapshot = usersRef.child(uid).get().await()
        return snapshot.getValue(User::class.java)
    }

    suspend fun createUser(uid: String, user: User) {
        usersRef.child(uid).setValue(user).await()
    }

    suspend fun updateUser(uid: String, user: User) {
        usersRef.child(uid).setValue(user).await()
    }

    suspend fun deleteUser(uid: String) {
        usersRef.child(uid).removeValue().await()
    }
}
