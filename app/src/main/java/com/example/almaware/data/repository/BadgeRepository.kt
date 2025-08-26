package com.example.almaware.data.repository

import com.example.almaware.data.model.Badge
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class BadgeRepository {

    private val db = FirebaseDatabase
        .getInstance("https://almaware-73b6b-default-rtdb.firebaseio.com/")
        .getReference("badges")
    private val badgesRef = db.child("badges")

    suspend fun getAllBadges(): List<Badge> {
        return try {
            val snapshot = db.get().await()
            snapshot.children.mapNotNull { child ->
                child.getValue(Badge::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getBadgeByKey(key: String): Badge? {
        return try {
            val snapshot = badgesRef.child(key).get().await()
            snapshot.getValue(Badge::class.java)
        } catch (e: Exception) {
            null
        }
    }

}
