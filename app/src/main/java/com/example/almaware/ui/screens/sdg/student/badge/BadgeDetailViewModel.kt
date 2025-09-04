package com.example.almaware.ui.screens.sdg.student.badge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almaware.data.model.Badge
import com.example.almaware.data.model.User
import com.example.almaware.data.repository.BadgeRepository
import com.example.almaware.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BadgeDetailViewModel(
    private val badgeRepository: BadgeRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    private val _allBadges = MutableStateFlow<Map<String, Badge>>(emptyMap())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val isLoading: StateFlow<Boolean> = _isLoading
    val error: StateFlow<String?> = _error

    init {
        loadBadges()
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firebaseAuth.currentUser?.uid?.let { uid ->
                    val user = userRepository.getUser(uid)
                    _currentUser.value = user
                }
            } catch (e: Exception) {
                _error.value = "Failed to load user data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadBadges() {
        viewModelScope.launch {
            try {
                val badges = badgeRepository.getAllBadges()
                _allBadges.value = badges
            } catch (e: Exception) {
                _error.value = "Failed to load badges: ${e.message}"
            }
        }
    }

    fun getCurrentUserFlow(): StateFlow<User?> = _currentUser

    fun getBadgeById(badgeId: String): Badge? {
        return _allBadges.value[badgeId]
    }

    fun getBadgeIdByName(badgeName: String): String? {
        return _allBadges.value.entries.find { (_, badge) ->
            badge.badgeName == badgeName
        }?.key
    }

    fun getBadgeProgress(badgeId: String): Any? {
        val badgeIndex = badgeId.toIntOrNull()?.minus(1) ?: return null
        return _currentUser.value?.badgeProgress?.getOrNull(badgeIndex)
    }

    fun incrementBadgeProgress(badgeId: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val badgeIndex = badgeId.toIntOrNull()?.minus(1) ?: return

        viewModelScope.launch {
            try {
                userRepository.incrementBadgeProgress(userId, badgeIndex)
                loadCurrentUser()
            } catch (e: Exception) {
                _error.value = "Failed to update badge progress: ${e.message}"
            }
        }
    }

    fun saveBadgeQuizAnswers(badgeId: String, answers: List<String>) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val badgeIndex = badgeId.toIntOrNull()?.minus(1) ?: return

        viewModelScope.launch {
            try {
                userRepository.saveBadgeQuizAnswers(userId, badgeIndex, answers)
                loadCurrentUser()
            } catch (e: Exception) {
                _error.value = "Failed to save quiz answers: ${e.message}"
            }
        }
    }

    // Controlla se un badge MultiCheckbox è completato
    fun isMultiCheckboxBadgeCompleted(badgeId: String): Boolean {
        val badge = getBadgeById(badgeId)
        if (badge?.type != "MultiCheckbox" || badge.targetCount == null) return false

        val badgeIndex = badgeId.toIntOrNull()?.minus(1) ?: return false
        val currentProgress = (_currentUser.value?.badgeProgress?.getOrNull(badgeIndex) as? Number)?.toInt() ?: 0

        return currentProgress >= badge.targetCount
    }

    // Controlla se un quiz è completato (≥80% di risposte corrette)
    fun isQuizBadgeCompleted(badgeId: String): Boolean {
        val badge = getBadgeById(badgeId)
        if (badge?.type != "Quizz") return false

        val badgeIndex = badgeId.toIntOrNull()?.minus(1) ?: return false
        val answers = (_currentUser.value?.badgeProgress?.getOrNull(badgeIndex) as? List<*>)?.mapNotNull { it as? String }

        if (answers.isNullOrEmpty() || answers.size != badge.questions.size) return false

        val score = calculateQuizScore(badge, answers)
        return score >= (badge.passingScore ?: 80)
    }

    // Calcola il punteggio del quiz
    private fun calculateQuizScore(badge: Badge, userAnswers: List<String>): Int {
        if (badge.questions.size != userAnswers.size) return 0

        var correctCount = 0
        badge.questions.forEachIndexed { index, question ->
            if (index < userAnswers.size) {
                val correctAnswerText = when (question.correctAnswer) {
                    1 -> question.answer1
                    2 -> question.answer2
                    3 -> question.answer3
                    4 -> question.answer4
                    else -> question.answer1
                }
                if (userAnswers[index] == correctAnswerText) {
                    correctCount++
                }
            }
        }

        return (correctCount * 100) / badge.questions.size
    }

    // Controlla se deve essere mostrato il popup di completamento
    fun shouldShowCompletionPopup(badgeId: String): Boolean {
        val badge = getBadgeById(badgeId) ?: return false

        return when (badge.type) {
            "MultiCheckbox" -> isMultiCheckboxBadgeCompleted(badgeId)
            "Quizz" -> isQuizBadgeCompleted(badgeId)
            else -> false
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun refreshData() {
        loadCurrentUser()
        loadBadges()
    }
}