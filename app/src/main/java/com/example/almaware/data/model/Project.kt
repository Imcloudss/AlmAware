package com.example.almaware.data.model

data class Project(
    val id: String = "",
    val name: String = "",
    val link: String = "",
    val sdgId: String = "",
    val keywords: Map<String, Boolean> = emptyMap()
)