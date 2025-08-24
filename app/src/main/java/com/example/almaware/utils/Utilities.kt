package com.example.almaware.utils

import com.example.almaware.R
import com.example.almaware.ui.screens.home.data.backgrounds
import com.example.almaware.ui.screens.home.data.borderColors
import com.example.almaware.ui.screens.home.data.cardOverlays
import com.example.almaware.data.model.HomeCard

fun generateCardById(id: Int): HomeCard {
    return HomeCard(
        id = id,
        backgroundRes = backgrounds[id - 1],
        overlayRes = cardOverlays[id - 1],
        borderColor = borderColors[id - 1],
        route = "destination$id"
    )
}

fun getDrawableForSdg(id: Int): Int {
    return when (id) {
        1 -> R.drawable.bubble1
        2 -> R.drawable.bubble2
        3 -> R.drawable.bubble3
        4 -> R.drawable.bubble4
        5 -> R.drawable.bubble5
        6 -> R.drawable.bubble6
        7 -> R.drawable.bubble7
        8 -> R.drawable.bubble8
        9 -> R.drawable.bubble9
        10 -> R.drawable.bubble10
        11 -> R.drawable.bubble11
        12 -> R.drawable.bubble12
        13 -> R.drawable.bubble13
        14 -> R.drawable.bubble14
        15 -> R.drawable.bubble15
        16 -> R.drawable.bubble16
        17 -> R.drawable.bubble17
        else -> R.drawable.bubble1
    }
}

