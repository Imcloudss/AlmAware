package com.example.almaware.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Utility function semplice per animazioni Lottie usando la libreria classica Airbnb
 * Supporta sia file .json che .lottie tramite URL
 *
 * @param animationUrl URL dell'animazione (.lottie o .json)
 * @param modifier Modifier per personalizzare il composable
 * @param size Dimensione dell'animazione (default 200dp)
 * @param iterations Numero di ripetizioni (LottieConstants.IterateForever per loop infinito)
 * @param speed Velocità dell'animazione (default 1f)
 */
@Composable
fun SimpleLottieAnimation(
    animationUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(animationUrl)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(size)
    )
}

/**
 * Versione con gestione errori e fallback
 */
@Composable
fun SafeLottieAnimation(
    animationUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f,
    fallbackEmoji: String = "🎬"
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(animationUrl)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed
    )

    if (composition != null) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier.size(size)
        )
    } else {
        // Fallback con emoji
        EmojiFallback(
            emoji = fallbackEmoji,
            size = size,
            modifier = modifier
        )
    }
}

/**
 * Versione one-shot (eseguita una sola volta)
 */
@Composable
fun LottieAnimationOneShot(
    animationUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    speed: Float = 1f,
    onComplete: () -> Unit = {}
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(animationUrl)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = speed
    )

    // Callback quando l'animazione finisce
    if (progress == 1f && composition != null) {
        onComplete()
    }

    if (composition != null) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier.size(size)
        )
    }
}

/**
 * Fallback con emoji per quando le animazioni non caricano
 */
@Composable
fun EmojiFallback(
    emoji: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = (size.value / 3).sp
        )
    }
}

// URL delle animazioni
object AnimationUrls {
    // Animazioni
    const val SPLASH_ANIMATION =
        "https://lottie.host/144d64cf-15b2-4ef5-816d-1f06a1dea320/IIykuP2BlK.lottie"
    const val LOGIN_ANIMATION =
        "https://lottie.host/95aaf697-45df-46a2-8a96-c7c63942bd00/nCJKa0SySQ.lottie"
}