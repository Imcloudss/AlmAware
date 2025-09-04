package com.example.almaware.ui.screens.sdg.unibo.clickable.project

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almaware.data.model.HomeCard
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import com.example.almaware.ui.composables.ComposeBubble
import com.example.almaware.ui.screens.auth.AuthViewModel
import com.example.almaware.ui.screens.sdg.VigaFontFamily
import com.example.almaware.ui.screens.sdg.unibo.UniboViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun ProjectsScreen(
    navController: NavController,
    item: HomeCard,
    projectId: Int,
    uniboViewModel: UniboViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val projects by uniboViewModel.projects.collectAsState()
    LaunchedEffect(Unit) {
        uniboViewModel.loadProjects()
    }
    val clickedProject = projects.find { it.id == projectId }

    Scaffold(
        topBar = { AppBar("Prove", navController, authViewModel) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        top = 100.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF0EDE8))
                ) {
                    clickedProject?.name?.let {
                        Text(
                            text = it,
                            fontFamily = VigaFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 28.sp,
                            modifier = Modifier.padding(
                                top = 125.dp,
                                start = 18.dp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5))
                            .height(370.dp)
                            .width(350.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            clickedProject?.keywords?.let { keywords ->
                                if (keywords.isNotEmpty()) {
                                    // Crea animazioni sovrapposte
                                    val numberOfAnimations = minOf(8, keywords.size * 2)

                                    repeat(numberOfAnimations) { index ->
                                        CyclingKeywordAnimation(
                                            keywords = keywords,
                                            animationIndex = index,
                                            color = Color(item.borderColor),
                                            // Sfasa il tempo di ogni animazione per creare sovrapposizione
                                            delayOffset = index * 800L
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ComposeBubble(
                item,
                "project",
                modifier = Modifier
                    .padding(top = 20.dp)
            )
        }
    }
}

object PositionManager {
    private val occupiedPositions = mutableListOf<Pair<IntRange, IntRange>>()

    private fun isPositionFree(x: Int, y: Int): Boolean {
        return occupiedPositions.none { (xRange, yRange) ->
            x in xRange && y in yRange
        }
    }

    fun occupyPosition(x: Int, y: Int, margin: Int = 80) {
        occupiedPositions.add(
            Pair(
                (x - margin)..(x + margin),
                (y - margin)..(y + margin)
            )
        )
    }

    fun releasePosition(x: Int, y: Int) {
        occupiedPositions.removeAll { (xRange, yRange) ->
            x in xRange && y in yRange
        }
    }

    fun findFreePosition(random: Random, maxAttempts: Int = 20): Pair<Int, Int>? {
        repeat(maxAttempts) {
            val x = random.nextInt(20, 280)
            val y = random.nextInt(20, 320)
            if (isPositionFree(x, y)) {
                return Pair(x, y)
            }
        }
        return null
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun CyclingKeywordAnimation(
    keywords: List<String>,
    animationIndex: Int,
    color: Color,
    delayOffset: Long = 0L
) {
    var currentKeywordIndex by remember { mutableIntStateOf(animationIndex % keywords.size) }
    val random = remember { Random(animationIndex) }

    var xOffset by remember { mutableStateOf(0.dp) }
    var yOffset by remember { mutableStateOf(0.dp) }
    var currentX by remember { mutableIntStateOf(0) }
    var currentY by remember { mutableIntStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "keyword_$animationIndex")

    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0 using FastOutSlowInEasing
                1f at 750 using LinearEasing
                1f at 2250 using LinearEasing
                0f at 3000 using FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(delayOffset.toInt())
        ),
        label = "alpha_$animationIndex"
    )

    // Trova una posizione libera iniziale
    LaunchedEffect(Unit) {
        val position = PositionManager.findFreePosition(random) ?: Pair(
            random.nextInt(20, 280),
            random.nextInt(20, 320)
        )
        currentX = position.first
        currentY = position.second
        xOffset = currentX.dp
        yOffset = currentY.dp
        PositionManager.occupyPosition(currentX, currentY)
    }

    // Gestisci il cambio di keyword e posizione
    LaunchedEffect(animationProgress) {
        if (animationProgress < 0.01f && currentX != 0) { // All'inizio di ogni ciclo
            // Rilascia la posizione precedente
            PositionManager.releasePosition(currentX, currentY)

            // Cambia keyword
            currentKeywordIndex = (currentKeywordIndex + 1) % keywords.size

            // Trova una nuova posizione libera
            val position = PositionManager.findFreePosition(random) ?: Pair(
                random.nextInt(20, 280),
                random.nextInt(20, 320)
            )
            currentX = position.first
            currentY = position.second
            xOffset = currentX.dp
            yOffset = currentY.dp
        } else if (animationProgress > 0.1f && animationProgress < 0.15f) {
            // Occupa la posizione quando inizia a diventare visibile
            PositionManager.occupyPosition(currentX, currentY)
        } else if (animationProgress < 0.9f && animationProgress > 0.85f) {
            // Rilascia la posizione quando inizia a svanire
            PositionManager.releasePosition(currentX, currentY)
        }
    }

    // Pulisci quando il composable viene distrutto
    DisposableEffect(Unit) {
        onDispose {
            PositionManager.releasePosition(currentX, currentY)
        }
    }

    if (animationProgress > 0f && keywords.isNotEmpty()) {
        Text(
            text = keywords[currentKeywordIndex],
            color = color.copy(alpha = animationProgress),
            fontSize = 16.sp,
            fontFamily = VigaFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.offset(x = xOffset, y = yOffset)
        )
    }
}