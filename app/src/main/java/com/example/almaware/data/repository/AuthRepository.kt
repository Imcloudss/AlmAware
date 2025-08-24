package com.example.almaware.data.repository

import com.example.almaware.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthRepository(
    private val auth: FirebaseAuth,
    private val db: DatabaseReference
) {

    // --- SIGN UP ---
    suspend fun signUp(username: String, email: String, password: String): User = suspendCancellableCoroutine { cont ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        cont.resumeWithException(Exception("User ID is null after signup"))
                        return@addOnCompleteListener
                    }

                    val newUser = User(
                        username = username,
                        email = email,
                    )

                    db.child("users").child(uid).setValue(newUser)
                        .addOnCompleteListener { saveTask ->
                            if (saveTask.isSuccessful) {
                                cont.resume(newUser)
                            } else {
                                cont.resumeWithException(saveTask.exception ?: Exception("Failed to save user data"))
                            }
                        }

                } else {
                    cont.resumeWithException(task.exception ?: Exception("Unknown signup error"))
                }
            }
    }

    // --- SIGN IN ---
    suspend fun signIn(email: String, password: String): User = suspendCancellableCoroutine { cont ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        cont.resumeWithException(Exception("User ID is null"))
                        return@addOnCompleteListener
                    }

                    db.child("users").child(uid).get()
                        .addOnSuccessListener { snapshot ->
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                cont.resume(user)
                            } else {
                                cont.resumeWithException(Exception("User data is null"))
                            }
                        }
                        .addOnFailureListener { e ->
                            cont.resumeWithException(e)
                        }

                } else {
                    cont.resumeWithException(task.exception ?: Exception("Unknown sign-in error"))
                }
            }
    }

    // --- LOGOUT ---
    fun signOut() {
        auth.signOut()
    }

    // --- GET CURRENT USER ---
    fun getCurrentUser(onResult: (user: User?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.child("users").child(uid).get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)
                    onResult(user)
                }
                .addOnFailureListener {
                    onResult(null)
                }
        } else {
            onResult(null)
        }
    }
}
