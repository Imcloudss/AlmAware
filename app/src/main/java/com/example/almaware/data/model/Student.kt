package com.example.almaware.data.model

data class Student(
    val id: String = "",
    val title: String = "",
    val goals: Map<String, Int> = emptyMap()
)