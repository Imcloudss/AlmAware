package com.example.almaware.ui.screens.sdg.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almaware.data.model.Badge
import com.example.almaware.data.model.User
import com.example.almaware.data.repository.BadgeRepository
import com.example.almaware.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

enum class BadgeFilter {
    ALL,
    COMPLETED,
    TO_COMPLETE,
    SDG
}

data class FilterState(
    val type: BadgeFilter = BadgeFilter.ALL,
    val sdgNumber: Int? = null
) {
    fun getDisplayText(): String = when(type) {
        BadgeFilter.ALL -> ""
        BadgeFilter.COMPLETED -> "Completed"
        BadgeFilter.TO_COMPLETE -> "To complete"
        BadgeFilter.SDG -> sdgNumber?.let { "SDG $it" } ?: "SDGs"
    }
}

data class BadgeWithUserStatus(
    val badgeId: String,
    val badge: Badge,
    val currentProgress: Any?,
    val progressPercentage: Float,
    val isCompleted: Boolean
)

class StudentViewModel(
    private val badgeRepository: BadgeRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _allBadges = MutableStateFlow<Map<String, Badge>>(emptyMap())
    private val _currentUser = MutableStateFlow<User?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    private val _currentFilter = MutableStateFlow(FilterState())
    val currentFilter: StateFlow<FilterState> = _currentFilter

    val isLoading: StateFlow<Boolean> = _isLoading
    val error: StateFlow<String?> = _error

    // Combina badge con lo stato dell'utente per determinare progresso e completamento
    private val badgesWithStatus: StateFlow<List<BadgeWithUserStatus>> = combine(
        _allBadges,
        _currentUser
    ) { badges, user ->
        badges.map { (badgeId, badge) ->
            val badgeIndex = badgeId.toIntOrNull()?.minus(1) // Converte "1" -> 0, "2" -> 1, etc.
            val currentProgress = user?.badgeProgress?.getOrNull(badgeIndex ?: -1)
            val progressPercentage = calculateProgressPercentage(badge, currentProgress)
            val isCompleted = progressPercentage >= 100f

            BadgeWithUserStatus(
                badgeId = badgeId,
                badge = badge,
                currentProgress = currentProgress,
                progressPercentage = progressPercentage,
                isCompleted = isCompleted
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Combina badge con stato utente e filtro corrente
    val filteredBadges: StateFlow<List<BadgeWithUserStatus>> = combine(
        badgesWithStatus,
        _currentFilter
    ) { badgesWithStatus, filter ->
        applyFilter(badgesWithStatus, filter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Per compatibilità - restituisce lista semplice di Badge
    val badges: StateFlow<List<Badge>> = _allBadges.map { it.values.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadCurrentUser()
        loadBadges()
    }

    fun getCurrentUserFlow(): StateFlow<User?> = _currentUser.asStateFlow()

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
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadBadges() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val badges = badgeRepository.getAllBadges()
                _allBadges.value = badges
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load badges: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateProgressPercentage(badge: Badge, currentProgress: Any?): Float {
        return when (badge.type) {
            "Quizz" -> {
                val answers = currentProgress as? List<*>
                if (answers != null && answers.isNotEmpty() && answers.all { it is String && it.toString().isNotEmpty() }) {
                    // Se ha risposto a tutte le domande, controlla se ha passato il quiz
                    val score = calculateQuizScore(badge, answers.mapNotNull { it as? String })
                    if (score >= (badge.passingScore ?: 80)) 100f else 0f
                } else 0f
            }
            "MultiCheckbox" -> {
                val current = (currentProgress as? Number)?.toInt() ?: 0
                val required = badge.targetCount ?: 1
                (current.toFloat() / required * 100).coerceAtMost(100f)
            }
            "Input" -> {
                when (currentProgress) {
                    is List<*> -> if ((currentProgress.getOrNull(0) as? String)?.isNotEmpty() == true) 100f else 0f
                    is String -> if (currentProgress.isNotEmpty()) 100f else 0f
                    else -> 0f
                }
            }
            else -> 0f
        }
    }

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

    private fun applyFilter(badgesWithStatus: List<BadgeWithUserStatus>, filter: FilterState): List<BadgeWithUserStatus> {
        return when (filter.type) {
            BadgeFilter.ALL -> badgesWithStatus
            BadgeFilter.COMPLETED -> badgesWithStatus.filter { it.isCompleted }
            BadgeFilter.TO_COMPLETE -> badgesWithStatus.filter { !it.isCompleted }
            BadgeFilter.SDG -> filter.sdgNumber?.let { sdgNumber ->
                badgesWithStatus.filter { it.badge.sdgRef == sdgNumber }
            } ?: badgesWithStatus
        }
    }

    fun setCompletedFilter() {
        val newFilter = if (_currentFilter.value.type == BadgeFilter.COMPLETED) {
            FilterState() // Deseleziona se già attivo
        } else {
            FilterState(BadgeFilter.COMPLETED)
        }
        _currentFilter.value = newFilter
    }

    fun setToCompleteFilter() {
        val newFilter = if (_currentFilter.value.type == BadgeFilter.TO_COMPLETE) {
            FilterState() // Deseleziona se già attivo
        } else {
            FilterState(BadgeFilter.TO_COMPLETE)
        }
        _currentFilter.value = newFilter
    }

    fun setSdgFilter(sdgNumber: Int) {
        _currentFilter.value = FilterState(BadgeFilter.SDG, sdgNumber)
    }

    fun clearFilter() {
        _currentFilter.value = FilterState()
    }

    private fun isFilterSelected(filterType: BadgeFilter): Boolean {
        return _currentFilter.value.type == filterType
    }

    fun isCompletedSelected(): Boolean = isFilterSelected(BadgeFilter.COMPLETED)
    fun isToCompleteSelected(): Boolean = isFilterSelected(BadgeFilter.TO_COMPLETE)
    fun isSdgSelected(): Boolean = isFilterSelected(BadgeFilter.SDG)

    fun getBadgesForSdg(sdgId: Int): List<Badge> {
        return _allBadges.value.values.filter { it.sdgRef == sdgId }
    }

    fun getBadgeById(badgeId: String): Badge? {
        return _allBadges.value[badgeId]
    }

    fun getCurrentUser(): User? = _currentUser.value

    fun refreshData() {
        loadCurrentUser()
        loadBadges()
    }

    fun clearError() {
        _error.value = null
    }

    // Metodi per aggiornare il progresso dei badge
    fun incrementBadgeProgress(badgeId: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val badgeIndex = badgeId.toIntOrNull()?.minus(1) ?: return

        viewModelScope.launch {
            try {
                userRepository.incrementBadgeProgress(userId, badgeIndex)
                // Ricarica i dati utente per aggiornare la UI
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

    fun saveBadgeInputData(badgeId: String, inputValue: String, numericValue: Double = 0.0) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val badgeIndex = badgeId.toIntOrNull()?.minus(1) ?: return

        viewModelScope.launch {
            try {
                userRepository.saveBadgeInputData(userId, badgeIndex, inputValue, numericValue)
                loadCurrentUser()
            } catch (e: Exception) {
                _error.value = "Failed to save input data: ${e.message}"
            }
        }
    }

    // Funzione per trovare il badgeId dal nome del badge
    fun getBadgeIdByName(badgeName: String): String? {
        return _allBadges.value.entries.find { (_, badge) ->
            badge.badgeName == badgeName
        }?.key
    }

    // Funzione per ottenere il progresso corrente di un badge
    fun getBadgeProgress(badgeId: String): Any? {
        val badgeIndex = badgeId.toIntOrNull()?.minus(1) ?: return null
        return _currentUser.value?.badgeProgress?.getOrNull(badgeIndex)
    }
}