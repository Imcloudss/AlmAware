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
    fun isActive(): Boolean = type != BadgeFilter.ALL

    fun getDisplayText(): String = when(type) {
        BadgeFilter.ALL -> ""
        BadgeFilter.COMPLETED -> "Completed"
        BadgeFilter.TO_COMPLETE -> "To complete"
        BadgeFilter.SDG -> sdgNumber?.let { "SDG $it" } ?: "SDGs"
    }
}

data class BadgeWithUserStatus(
    val badge: Badge,
    val isCompleted: Boolean
)

class StudentViewModel(
    private val badgeRepository: BadgeRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _allBadges = MutableStateFlow<List<Badge>>(emptyList())
    private val _currentUser = MutableStateFlow<User?>(null)

    private val _currentFilter = MutableStateFlow(FilterState())
    val currentFilter: StateFlow<FilterState> = _currentFilter

    // Combina badge con lo stato dell'utente per determinare quali sono completati
    private val badgesWithStatus: StateFlow<List<BadgeWithUserStatus>> = combine(
        _allBadges,
        _currentUser
    ) { badges, user ->
        badges.mapIndexed { index, badge ->
            // Usa l'indice del badge nella lista per accedere al badgeStatus
            val isCompleted = user?.badgeStatus?.getOrNull(index) == 1
            BadgeWithUserStatus(badge, isCompleted)
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

    // Mantieni questo per compatibilità, ma ora restituisce Badge semplici
    val badges: StateFlow<List<Badge>> = _allBadges

    init {
        loadCurrentUser()
    }

    suspend fun getBadgeByFirebaseKey(badgeKey: String): Badge? {
        return try {
            badgeRepository.getBadgeByKey(badgeKey)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            firebaseAuth.currentUser?.uid?.let { uid ->
                try {
                    val user = userRepository.getUser(uid)
                    _currentUser.value = user
                } catch (e: Exception) {
                    // Gestisci errore di caricamento utente
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadBadges() {
        viewModelScope.launch {
            try {
                _allBadges.value = badgeRepository.getAllBadges()
            } catch (e: Exception) {
                // Gestisci errore di caricamento badge
                e.printStackTrace()
            }
        }
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

    // Metodo per aggiornare lo stato di completamento di un badge
    fun toggleBadgeCompletion(badgeIndex: Int) {
        viewModelScope.launch {
            val currentUser = _currentUser.value
            val uid = firebaseAuth.currentUser?.uid

            if (currentUser != null && uid != null && badgeIndex >= 0 && badgeIndex < currentUser.badgeStatus.size) {
                val currentStatus = currentUser.badgeStatus.toMutableList()

                // Toggle tra 1 (completato) e 0 (non completato) all'indice specificato
                currentStatus[badgeIndex] = if (currentStatus[badgeIndex] == 1) 0 else 1

                val updatedUser = currentUser.copy(badgeStatus = currentStatus)

                try {
                    userRepository.updateUser(uid, updatedUser)
                    _currentUser.value = updatedUser
                } catch (e: Exception) {
                    // Gestisci errore di aggiornamento
                    e.printStackTrace()
                }
            }
        }
    }

    // Metodo per ottenere il numero di badge completati
    fun getCompletedBadgesCount(): Int {
        return _currentUser.value?.badgeStatus?.count { it == 1 } ?: 0
    }

    // Metodo per ottenere il numero totale di badge
    fun getTotalBadgesCount(): Int {
        return _allBadges.value.size
    }

    // Mantieni questo metodo per compatibilità
    fun getBadgesForSdg(sdgId: Int): List<Badge> {
        return _allBadges.value.filter { it.sdgRef == sdgId }
    }
}