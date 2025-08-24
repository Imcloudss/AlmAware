package com.example.almaware.ui.screens.flower

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almaware.data.model.User
import com.example.almaware.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class PlantType(
    val id: String,
    val imageUrl: String,
    val name: String
)

data class FlowerUiState(
    val flowerName: String = "",
    val selectedColorIndex: Int = -1, // -1 = non selezionato, userà default
    val selectedFlowerType: String = "",
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val isSavingName: Boolean = false,
    val nameSaved: Boolean = false,
    val availablePlants: List<PlantType> = emptyList(),
    val plantsLoading: Boolean = false
)

class FlowerViewModel(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlowerUiState())
    val uiState: StateFlow<FlowerUiState> = _uiState.asStateFlow()

    // Colori SDG
    val potColors = listOf(
        0xFFE5243BL,
        0xFFDDA63AL,
        0xFF4C9F38L,
        0xFFC5192DL,
        0xFFFF3A21L,
        0xFF26BDE2L,
        0xFFFCC30BL,
        0xFFA21942L,
        0xFFFD6925L,
        0xFFDD1367L,
        0xFFFD9D24L,
        0xFFBF8B2EL,
        0xFF3F7E44L,
        0xFF0A97D9L,
        0xFF56C02BL,
        0xFF00689DL,
        0xFF19486AL,
    )

    // Job per il debounce del salvataggio del nome
    private var saveNameJob: Job? = null

    init {
        loadUserData()
        loadAvailablePlants()
    }

    private fun loadAvailablePlants() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(plantsLoading = true)

            try {
                val plantsRef = storage.reference.child("plants")
                val plantsList = mutableListOf<PlantType>()

                // Metodo 1: Usa listAll per ottenere tutti i file nella cartella
                try {
                    val result = plantsRef.listAll().await()

                    for (item in result.items) {
                        try {
                            val downloadUrl = item.downloadUrl.await()
                            val fileName = item.name

                            plantsList.add(
                                PlantType(
                                    id = fileName.removeSuffix(".png"),
                                    imageUrl = downloadUrl.toString(),
                                    name = fileName.removeSuffix(".png")
                                )
                            )

                            println("DEBUG: Caricata immagine $fileName con URL: $downloadUrl")
                        } catch (e: Exception) {
                            println("ERROR: Impossibile ottenere URL per ${item.name}: ${e.message}")
                        }
                    }
                } catch (e: Exception) {
                    // Se listAll fallisce, proviamo il metodo manuale
                    println("DEBUG: listAll fallito, provo metodo manuale. Errore: ${e.message}")

                    val plantFiles = listOf("plant.png", "plant2.png", "plant3.png", "plant4.png")

                    for (fileName in plantFiles) {
                        try {
                            val imageRef = plantsRef.child(fileName)
                            val downloadUrl = imageRef.downloadUrl.await()

                            plantsList.add(
                                PlantType(
                                    id = fileName.removeSuffix(".png"),
                                    imageUrl = downloadUrl.toString(),
                                    name = fileName.removeSuffix(".png")
                                )
                            )

                            println("DEBUG: Caricata manualmente $fileName")
                        } catch (e: Exception) {
                            println("ERROR: Impossibile caricare $fileName: ${e.message}")
                        }
                    }
                }

                println("DEBUG: Totale piante caricate: ${plantsList.size}")

                _uiState.value = _uiState.value.copy(
                    availablePlants = plantsList,
                    plantsLoading = false
                )
            } catch (e: Exception) {
                println("ERROR GENERALE: ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(plantsLoading = false)
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            auth.currentUser?.let { firebaseUser ->
                try {
                    val user = userRepository.getUser(firebaseUser.uid)
                    user?.let {
                        _uiState.value = _uiState.value.copy(
                            currentUser = it,
                            selectedColorIndex = if (it.pot.color > 0) it.pot.color - 1 else -1,
                            flowerName = it.pot.type,
                            selectedFlowerType = it.pot.type,
                            isLoading = false
                        )
                    }
                } catch (e: Exception) {
                    // Log error
                    println("ERROR loadUserData: ${e.message}")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } ?: run {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun updateFlowerName(name: String) {
        _uiState.value = _uiState.value.copy(flowerName = name)

        // Cancella il job precedente se esiste
        saveNameJob?.cancel()

        // Avvia un nuovo job con delay (debounce)
        saveNameJob = viewModelScope.launch {
            delay(800) // Aspetta 800ms dopo che l'utente smette di digitare
            saveFlowerName(name)
        }
    }

    private fun saveFlowerName(name: String) {
        viewModelScope.launch {
            auth.currentUser?.let { firebaseUser ->
                try {
                    _uiState.value = _uiState.value.copy(isSavingName = true, nameSaved = false)

                    val currentUser = _uiState.value.currentUser ?: userRepository.getUser(firebaseUser.uid)

                    currentUser?.let { user ->
                        // Salva il nome nel campo 'type' del Pot
                        val updatedPot = user.pot.copy(type = name)
                        val updatedUser = user.copy(pot = updatedPot)

                        userRepository.updateUser(firebaseUser.uid, updatedUser)
                        _uiState.value = _uiState.value.copy(
                            currentUser = updatedUser,
                            isSavingName = false,
                            nameSaved = true
                        )

                        // Rimuovi l'indicatore "salvato" dopo 2 secondi
                        delay(2000)
                        _uiState.value = _uiState.value.copy(nameSaved = false)
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(isSavingName = false, nameSaved = false)
                    println("ERROR saveFlowerName: ${e.message}")
                }
            }
        }
    }

    fun selectColor(index: Int) {
        _uiState.value = _uiState.value.copy(selectedColorIndex = index)
        savePotColor(index)
    }

    private fun savePotColor(colorIndex: Int) {
        viewModelScope.launch {
            auth.currentUser?.let { firebaseUser ->
                try {
                    val currentUser = _uiState.value.currentUser ?: userRepository.getUser(firebaseUser.uid)

                    currentUser?.let { user ->
                        val updatedPot = user.pot.copy(
                            color = colorIndex + 1 // +1 perché nel DB 0 significa "non impostato"
                        )
                        val updatedUser = user.copy(pot = updatedPot)

                        userRepository.updateUser(firebaseUser.uid, updatedUser)
                        _uiState.value = _uiState.value.copy(currentUser = updatedUser)
                    }
                } catch (e: Exception) {
                    println("ERROR savePotColor: ${e.message}")
                }
            }
        }
    }

    fun selectFlowerType(type: String) {
        _uiState.value = _uiState.value.copy(selectedFlowerType = type)
        saveFlowerType(type)
    }

    private fun saveFlowerType(type: String) {
        viewModelScope.launch {
            auth.currentUser?.let { firebaseUser ->
                try {
                    val currentUser = _uiState.value.currentUser ?: userRepository.getUser(firebaseUser.uid)

                    currentUser?.let { user ->
                        val updatedPot = user.pot.copy(type = type)
                        val updatedUser = user.copy(pot = updatedPot)

                        userRepository.updateUser(firebaseUser.uid, updatedUser)
                        _uiState.value = _uiState.value.copy(currentUser = updatedUser)
                    }
                } catch (e: Exception) {
                    println("ERROR saveFlowerType: ${e.message}")
                }
            }
        }
    }

    // Metodo pubblico per salvare immediatamente (utile per onFocusLost)
    fun saveFlowerNameNow() {
        saveNameJob?.cancel() // Cancella eventuali salvataggi pendenti
        viewModelScope.launch {
            saveFlowerName(_uiState.value.flowerName)
        }
    }

    // Metodo helper per ottenere il colore corrente del vaso
    fun getCurrentPotColor(): Long {
        return when (val index = _uiState.value.selectedColorIndex) {
            -1 -> 0xFFDD1367L // Colore default se mai personalizzato
            in 0..16 -> potColors[index]
            else -> 0xFFDD1367L
        }
    }
}