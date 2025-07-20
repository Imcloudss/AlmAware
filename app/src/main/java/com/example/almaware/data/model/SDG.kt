package com.example.almaware.data.model

data class SDGDescription(
    val number: String = "",
    val unit: String = "",
    val text: String = ""
)

data class SDG(
    val id: Int = 0,
    val title: String = "",
    val subtitle: String = "",
    val description: List<SDGDescription> = emptyList(),
    val course_units: Int = 0,
    val publications_unibo: Int = 0,
    val image: String = "",
    val background: String = "",
    val desc_kiosk: String = ""
)