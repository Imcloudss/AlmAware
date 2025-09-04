package com.example.almaware.data.repository

import com.example.almaware.data.model.Badge
import com.example.almaware.data.model.QuizQuestion
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class BadgeRepository {

    private val db = FirebaseDatabase
        .getInstance("https://almaware-73b6b-default-rtdb.firebaseio.com/")
        .getReference("badges")

    suspend fun getAllBadges(): Map<String, Badge> {
        return try {
            val snapshot = db.get().await()
            val badgesMap = mutableMapOf<String, Badge>()

            snapshot.children.forEach { child ->
                val badgeId = child.key
                val badge = child.getValue(Badge::class.java)
                if (badgeId != null && badge != null) {
                    badgesMap[badgeId] = badge
                }
            }

            badgesMap
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

    private suspend fun getBadgeById(badgeId: String): Badge? {
        return try {
            val snapshot = db.child(badgeId).get().await()
            snapshot.getValue(Badge::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getBadgesBySDG(sdgRef: Int): List<Badge> {
        return try {
            val allBadges = getAllBadges()
            allBadges.values.filter { it.sdgRef == sdgRef }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getBadgesByType(type: String): List<Badge> {
        return try {
            val allBadges = getAllBadges()
            allBadges.values.filter { it.type == type }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Metodi utility per la gestione dei quiz
    suspend fun getQuizQuestions(badgeId: String): List<QuizQuestion> {
        return try {
            val badge = getBadgeById(badgeId)
            badge?.questions ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Metodo per verificare se un badge è di tipo quiz
    suspend fun isQuizBadge(badgeId: String): Boolean {
        return try {
            val badge = getBadgeById(badgeId)
            badge?.type == "Quizz"
        } catch (e: Exception) {
            false
        }
    }

    // Metodo per verificare se un badge è di tipo MultiCheckbox
    suspend fun isMultiCheckboxBadge(badgeId: String): Boolean {
        return try {
            val badge = getBadgeById(badgeId)
            badge?.type == "MultiCheckbox"
        } catch (e: Exception) {
            false
        }
    }

    // Metodo per verificare se un badge è di tipo Input
    suspend fun isInputBadge(badgeId: String): Boolean {
        return try {
            val badge = getBadgeById(badgeId)
            badge?.type == "Input"
        } catch (e: Exception) {
            false
        }
    }

    // Metodo per ottenere il target count di un badge
    suspend fun getBadgeTargetCount(badgeId: String): Int {
        return try {
            val badge = getBadgeById(badgeId)
            badge?.targetCount ?: 1
        } catch (e: Exception) {
            1
        }
    }

    // Metodo per ottenere il punteggio di passaggio di un quiz
    suspend fun getBadgePassingScore(badgeId: String): Int {
        return try {
            val badge = getBadgeById(badgeId)
            badge?.passingScore ?: 80
        } catch (e: Exception) {
            80
        }
    }
}