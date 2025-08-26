package com.example.almaware.data.model

data class Project(
    val id: Int = 0,
    val name: String = "",
    val link: String = "",
    val sdgId: String = "",
    val keywords: List<String> = emptyList()
)