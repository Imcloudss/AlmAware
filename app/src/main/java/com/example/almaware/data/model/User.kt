package com.example.almaware.data.model

// Data class per l'utente basata sulla struttura JSON
data class User(
    val username: String = "",
    val email: String = "",
    val badgeProgress: List<Any> = List(15) { 0 },
    val pot: Pot = Pot()
)

// Data class per il pot dell'utente
data class Pot(
    val name: String = "",
    val color: Int = 0,
    val type: String = ""
)