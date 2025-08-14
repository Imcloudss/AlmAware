// File: UniboViewModel.kt
package com.example.almaware.ui.screens.sdg.unibo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.almaware.data.repository.ProjectRepository

class UniboViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _currentSdgId = MutableLiveData<String>()

    val projectCount: LiveData<Int> = _currentSdgId.switchMap { sdgId ->
        if (sdgId.isNotEmpty()) {
            projectRepository.getProjectCountBySdgId(sdgId)
        } else {
            MutableLiveData(0)
        }
    }

    fun loadProjectCountForSdg(sdgId: String) {
        _currentSdgId.value = sdgId
    }
}