package com.example.almaware.ui.composables

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import kotlin.random.Random

data class ConfettiParticle(
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float,
    var currentX: Float = startX,
    var currentY: Float = startY,
    var rotation: Float = 0f,
    var alpha: Float = 1f
)

@Composable
fun ConfettiAnimation(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    particleCount: Int = 50
) {
    LocalDensity.current
    var particles by remember { mutableStateOf(emptyList<ConfettiParticle>()) }

    rememberInfiniteTransition(label = "")

    // Colori per i coriandoli
    val confettiColors = listOf(
        Color(0xFFFF6B6B), // Rosso
        Color(0xFF4ECDC4), // Turchese
        Color(0xFF45B7D1), // Blu
        Color(0xFF96CEB4), // Verde
        Color(0xFFFECA57), // Giallo
        Color(0xFFFF9FF3), // Rosa
        Color(0xFFFF7675), // Rosso chiaro
        Color(0xFF6C5CE7), // Violetto
        Color(0xFFA29BFE), // Lavanda
        Color(0xFFFF8A65)  // Arancione
    )

    LaunchedEffect(isVisible) {
        if (isVisible) {
            particles = List(particleCount) {
                createRandomParticle(confettiColors)
            }
        }
    }

    if (isVisible && particles.isNotEmpty()) {
        Canvas(modifier = modifier.fillMaxSize()) {
            updateAndDrawParticles(
                particles = particles,
                canvasSize = size,
                onUpdate = { updatedParticles ->
                    particles = updatedParticles
                }
            )
        }
    }
}

private fun createRandomParticle(colors: List<Color>): ConfettiParticle {
    val random = Random.Default
    return ConfettiParticle(
        startX = random.nextFloat() * 400f + 200f, // Centrati orizzontalmente (200-600px)
        startY = -50f, // Inizia sopra il bordo superiore della finestra
        velocityX = (random.nextFloat() - 0.5f) * 100f, // Movimento orizzontale più contenuto
        velocityY = random.nextFloat() * 50f + 50f, // Velocità verso il basso (sempre positiva)
        color = colors.random(),
        size = random.nextFloat() * 12f + 8f, // Dimensione tra 8 e 20 (più grandi)
        rotationSpeed = (random.nextFloat() - 0.5f) * 360f // Velocità rotazione
    )
}

private fun DrawScope.updateAndDrawParticles(
    particles: List<ConfettiParticle>,
    canvasSize: Size,
    onUpdate: (List<ConfettiParticle>) -> Unit
) {
    val gravity = 98f // Accelerazione di gravità
    val deltaTime = 0.016f // ~60 FPS

    val updatedParticles = particles.map { particle ->
        // Aggiorna posizione con gravità
        val newVelocityY = particle.velocityY + gravity * deltaTime
        val newX = particle.currentX + particle.velocityX * deltaTime
        val newY = particle.currentY + newVelocityY * deltaTime

        // Aggiorna rotazione
        val newRotation = particle.rotation + particle.rotationSpeed * deltaTime

        // Calcola alpha basato sulla posizione (dissolvenza quando esce dallo schermo)
        val newAlpha = when {
            newY > canvasSize.height + 50f -> 0f
            newY > canvasSize.height - 100f -> 1f - ((newY - (canvasSize.height - 100f)) / 150f)
            else -> 1f
        }.coerceIn(0f, 1f)

        particle.copy(
            currentX = newX,
            currentY = newY,
            velocityY = newVelocityY,
            rotation = newRotation,
            alpha = newAlpha
        )
    }

    onUpdate(updatedParticles)

    // Disegna i coriandoli
    updatedParticles.forEach { particle ->
        if (particle.alpha > 0f) {
            rotate(particle.rotation, Offset(particle.currentX, particle.currentY)) {
                // Disegna rettangolo colorato (coriandolo)
                drawRect(
                    color = particle.color.copy(alpha = particle.alpha),
                    topLeft = Offset(
                        particle.currentX - particle.size / 2f,
                        particle.currentY - particle.size / 2f
                    ),
                    size = Size(
                        particle.size,
                        particle.size * 0.6f
                    )
                )
            }
        }
    }
}