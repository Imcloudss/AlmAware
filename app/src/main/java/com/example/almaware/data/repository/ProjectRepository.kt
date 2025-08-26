package com.example.almaware.data.repository

import com.example.almaware.data.model.Project
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class ProjectRepository {
    private val db = FirebaseDatabase
        .getInstance("https://almaware-73b6b-default-rtdb.firebaseio.com/")
        .getReference("projects")

    suspend fun getAllProjects(): List<Project> {
        return try {
            val snapshot = db.get().await()
            snapshot.children.mapNotNull { child ->
                child.getValue(Project::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
