package com.example.almaware.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.almaware.data.model.Keyword
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class KeywordRepository {
    private val db = FirebaseDatabase
        .getInstance("https://almaware-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference("keywords")

    fun getAllKeywords(): LiveData<List<Keyword>> {
        val result = MutableLiveData<List<Keyword>>()
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val keywords = snapshot.children.mapNotNull { it.getValue(Keyword::class.java)?.copy(id = it.key ?: "") }
                result.postValue(keywords)
            }

            override fun onCancelled(error: DatabaseError) {
                result.postValue(emptyList())
            }
        })
        return result
    }
}
