package com.example.almaware.ui.screens.sdg

import android.util.Log
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
        Log.d("SdgViewModel", "Loading SDG with id: $id")

        repository.getSdgById(id) { sdgData ->
            _sdg.value = sdgData

            // Log di debug per verificare che gli objectives siano stati caricati
            if (sdgData != null) {
                Log.d("SdgViewModel", "SDG loaded: ${sdgData.title}")
                Log.d("SdgViewModel", "Objectives count: ${sdgData.objectives.size}")
                if (sdgData.objectives.isNotEmpty()) {
                    Log.d("SdgViewModel", "First objective: ${sdgData.objectives.first()}")
                }
            } else {
                Log.e("SdgViewModel", "Failed to load SDG with id: $id")
            }
        }
    }
}