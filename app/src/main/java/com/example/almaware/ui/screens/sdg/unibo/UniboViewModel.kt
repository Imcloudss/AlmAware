package com.example.almaware.ui.screens.sdg.unibo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almaware.data.model.Project
import com.example.almaware.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UniboViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _currentSdgId = MutableLiveData<String>()
    private val _allProjects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _allProjects

    fun loadProjects() {
        viewModelScope.launch {
            try {
                val list = projectRepository.getAllProjects()
                Log.d("UniboViewModel", "Projects fetched from Firebase: $list")
                _allProjects.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getProjectCountForSdg(sdgId: String): Int {
        return _allProjects.value.count { it.sdgId.trim() == sdgId.trim() }
    }
}