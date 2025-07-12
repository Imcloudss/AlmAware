package com.example.almaware.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.almaware.data.model.Badge
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BadgeRepository {
    private val db = FirebaseDatabase
        .getInstance("https://almaware-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference("badges")

    fun getAllBadges(): LiveData<List<Badge>> {
        val result = MutableLiveData<List<Badge>>()
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val badges = snapshot.children.mapNotNull { it.getValue(Badge::class.java)?.copy(id = it.key ?: "") }
                result.postValue(badges)
            }

            override fun onCancelled(error: DatabaseError) {
                result.postValue(emptyList())
            }
        })
        return result
    }
}
