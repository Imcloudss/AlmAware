package com.example.almaware.data.repository

import com.example.almaware.data.model.SDG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SDGRepository {
    private val db = FirebaseDatabase
        .getInstance("https://almaware-73b6b-default-rtdb.firebaseio.com/")
        .getReference("sdgs")

    fun getSdgById(id: Int, onResult: (SDG?) -> Unit) {
        db.child(id.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val sdg = snapshot.getValue(SDG::class.java)
                    onResult(sdg)
                } catch (e: Exception) {
                    onResult(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }
}