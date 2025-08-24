package com.example.almaware.data.repository

import com.example.almaware.data.model.Badge
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    fun findBadgeKeyByName(badgeName: String, callback: (String?) -> Unit) {
        db.child("badges")
            .orderByChild("badgeName")
            .equalTo(badgeName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val firstChild = snapshot.children.first()
                        val badgeKey = firstChild.key
                        callback(badgeKey)
                    } else {
                        callback(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }
}
