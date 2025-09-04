package com.example.almaware.data.repository

import com.example.almaware.data.model.User
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await

class UserRepository(
    db: DatabaseReference
) {
    private val usersRef = db.child("users")

    suspend fun getUser(uid: String): User? {
        return try {
            val snapshot = usersRef.child(uid).get().await()
            snapshot.getValue(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createUser(uid: String, user: User) {
        try {
            usersRef.child(uid).setValue(user).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun updateUser(uid: String, user: User) {
        try {
            usersRef.child(uid).setValue(user).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun deleteUser(uid: String) {
        try {
            usersRef.child(uid).removeValue().await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // Metodo specifico per aggiornare il progresso di un badge
    private suspend fun updateBadgeProgress(uid: String, badgeIndex: Int, progress: Any) {
        try {
            usersRef.child(uid)
                .child("badgeProgress")
                .child(badgeIndex.toString())
                .setValue(progress)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // Metodo per aggiornare solo il pot dell'utente
    suspend fun updateUserPot(uid: String, pot: com.example.almaware.data.model.Pot) {
        try {
            usersRef.child(uid)
                .child("pot")
                .setValue(pot)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // Metodo per incrementare il progresso di un badge MultiCheckbox
    suspend fun incrementBadgeProgress(uid: String, badgeIndex: Int) {
        try {
            val snapshot = usersRef.child(uid)
                .child("badgeProgress")
                .child(badgeIndex.toString())
                .get()
                .await()

            val currentProgress = (snapshot.value as? Long)?.toInt() ?: 0
            updateBadgeProgress(uid, badgeIndex, currentProgress + 1)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // Metodo per salvare le risposte di un quiz
    suspend fun saveBadgeQuizAnswers(uid: String, badgeIndex: Int, answers: List<String>) {
        try {
            updateBadgeProgress(uid, badgeIndex, answers)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // Metodo per salvare input di un badge Input
    suspend fun saveBadgeInputData(uid: String, badgeIndex: Int, inputValue: String, numericValue: Double = 0.0) {
        try {
            val inputData = listOf(inputValue, numericValue)
            updateBadgeProgress(uid, badgeIndex, inputData)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}