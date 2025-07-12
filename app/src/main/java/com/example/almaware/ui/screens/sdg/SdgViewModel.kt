package com.example.almaware.ui.screens.sdg

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.example.almaware.data.model.SDG
import com.example.almaware.data.repository.SDGRepository

class SdgViewModel(
    private val repository: SDGRepository
) : ViewModel() {

    private val _sdg = mutableStateOf<SDG?>(null)
    val sdg: State<SDG?> = _sdg

    fun loadSdgById(id: Int) {
        repository.getSdgById(id) {
            _sdg.value = it
        }
    }
}
