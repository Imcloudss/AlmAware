package com.example.almaware.data.repository

import com.example.almaware.data.model.SDG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SDGRepository {
    private val db = FirebaseDatabase
        .getInstance("https://almaware-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference("sdgs")

    fun getSdgById(id: Int, onResult: (SDG?) -> Unit) {
        db.child(id.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sdg = snapshot.getValue(SDG::class.java)
                onResult(sdg)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }
}