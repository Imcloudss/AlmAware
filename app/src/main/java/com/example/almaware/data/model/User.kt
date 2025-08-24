package com.example.almaware.data.model

data class User(
    val username: String = "",
    val email: String = "",
    val badgeStatus: List<Int> = List(16) { 0 },
    val pot: Pot = Pot()
)

data class Pot(
    val name: String = "",
    val color: Int = 0,
    val type: String = ""
)