package com.example.almaware.data.model

data class Badge(
    val badgeName: String = "",
    val sdgRef: Int = 0,
    val points: Int = 0,
    val subtitle: String = "",
    val description: String = "",
    val validate: String = "",
    val unvalidate: String = "",
    val bubble: String = "",
    val icon: String = "",
    val type: String = "",
    val checkbox: Int? = null,
    val actionName: String = "",
    val howCheck: String = ""
)