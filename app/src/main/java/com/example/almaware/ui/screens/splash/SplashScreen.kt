package com.example.almaware.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.almaware.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // Animazione di fade-in
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(true) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = LinearEasing
            )
        )
        delay(1200) // Attendi il resto del tempo (2s totali)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFbb2e29)), // Sfondo rosso
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_mascotte),
            contentDescription = "Mascotte",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .alpha(alpha.value)
        )
    }
}
