package com.example.almaware.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.almaware.data.model.Project
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProjectRepository {
    private val db = FirebaseDatabase
        .getInstance("https://almaware-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference("projects")

    fun getAllProjects(): LiveData<List<Project>> {
        val result = MutableLiveData<List<Project>>()
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val projects = snapshot.children.mapNotNull { it.getValue(Project::class.java)?.copy(id = it.key ?: "") }
                result.postValue(projects)
            }

            override fun onCancelled(error: DatabaseError) {
                result.postValue(emptyList())
            }
        })
        return result
    }
}
