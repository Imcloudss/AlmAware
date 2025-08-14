package com.example.almaware.ui.screens.sdg.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almaware.data.model.Badge
import com.example.almaware.data.repository.BadgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentViewModel(
    private val repository: BadgeRepository
) : ViewModel() {

    private val _badges = MutableStateFlow<List<Badge>>(emptyList())
    val badges: StateFlow<List<Badge>> = _badges

    fun loadBadges() {
        viewModelScope.launch {
            _badges.value = repository.getAllBadges()
        }
    }

    fun getBadgesForSdg(sdgId: Int): List<Badge> {
        return _badges.value.filter { it.sdgRef == sdgId }
    }
}
