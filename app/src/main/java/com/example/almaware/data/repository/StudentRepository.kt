package com.example.almaware.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.almaware.data.model.Student
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentRepository {

    private val db = FirebaseDatabase
        .getInstance("https://almaware-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference("students")

    fun getAllStudents(): LiveData<List<Student>> {
        val result = MutableLiveData<List<Student>>()
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val students = snapshot.children.mapNotNull { it.getValue(Student::class.java)?.copy(id = it.key ?: "") }
                result.postValue(students)
            }

            override fun onCancelled(error: DatabaseError) {
                result.postValue(emptyList())
            }
        })
        return result
    }
}
