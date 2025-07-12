package com.example.almaware.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeCard(
    val id: Int,
    val backgroundRes: Int,
    val overlayRes: Int,
    val borderColor: Long,
    val route: String,
)