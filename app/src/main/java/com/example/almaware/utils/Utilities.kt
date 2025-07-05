package com.example.almaware.utils

import com.example.almaware.ui.screens.home.HomeCard
import com.example.almaware.ui.screens.home.data.backgrounds
import com.example.almaware.ui.screens.home.data.borderColors
import com.example.almaware.ui.screens.home.data.cardOverlays

fun generateCardById(id: Int): HomeCard {
    return HomeCard(
        id = id,
        backgroundRes = backgrounds[id - 1],
        overlayRes = cardOverlays[id - 1],
        borderColor = borderColors[id - 1],
        route = "destination$id"
    )
}
