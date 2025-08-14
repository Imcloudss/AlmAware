package com.example.almaware.data.repository

import android.util.Log
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
        Log.d("SDGRepository", "Fetching SDG with id: $id")

        db.child(id.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    // Prima prova il parsing automatico
                    var sdg = snapshot.getValue(SDG::class.java)

                    // Se il parsing ha funzionato ma objectives è vuoto, lo correggiamo manualmente
                    if (sdg != null) {
                        // Controlla se objectives è vuoto e prova a parsarlo manualmente
                        if (sdg.objectives.isEmpty()) {
                            val objectives = mutableListOf<String>()

                            // Firebase salva gli array come oggetti con chiavi numeriche (0, 1, 2, ...)
                            val objectivesSnapshot = snapshot.child("objectives")
                            if (objectivesSnapshot.exists()) {
                                objectivesSnapshot.children.forEach { child ->
                                    val objective = child.getValue(String::class.java)
                                    if (objective != null) {
                                        objectives.add(objective)
                                        Log.d("SDGRepository", "Added objective: $objective")
                                    }
                                }
                            }

                            // Ricrea l'SDG con gli objectives corretti usando copy
                            sdg = sdg.copy(objectives = objectives)
                        }

                        Log.d("SDGRepository", "Successfully loaded SDG ${sdg.id}: ${sdg.title}")
                        Log.d("SDGRepository", "Objectives count: ${sdg.objectives.size}")
                        sdg.objectives.forEachIndexed { index, obj ->
                            Log.d("SDGRepository", "Objective $index: $obj")
                        }
                    } else {
                        Log.w("SDGRepository", "Failed to parse SDG with id: $id")
                    }

                    onResult(sdg)
                } catch (e: Exception) {
                    Log.e("SDGRepository", "Error parsing SDG", e)
                    onResult(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SDGRepository", "Database error: ${error.message}")
                onResult(null)
            }
        })
    }

    // Metodo aggiuntivo per debug - puoi rimuoverlo dopo aver verificato che funziona
    fun debugObjectives(id: Int) {
        db.child(id.toString()).child("objectives")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("SDGRepository-DEBUG", "Raw objectives value: ${snapshot.value}")
                    Log.d("SDGRepository-DEBUG", "Objectives exists: ${snapshot.exists()}")
                    Log.d("SDGRepository-DEBUG", "Children count: ${snapshot.childrenCount}")

                    snapshot.children.forEach { child ->
                        Log.d("SDGRepository-DEBUG", "Key: ${child.key}, Value: ${child.value}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("SDGRepository-DEBUG", "Error: ${error.message}")
                }
            })
    }
}