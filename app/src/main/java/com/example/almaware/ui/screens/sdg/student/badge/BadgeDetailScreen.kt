package com.example.almaware.ui.screens.sdg.student.badge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.almaware.data.model.Badge
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BadgeCompletionPopup
import com.example.almaware.ui.composables.BottomNavigationBar
import com.example.almaware.ui.composables.QuizPopup
import com.example.almaware.ui.screens.auth.AuthViewModel
import com.example.almaware.ui.screens.sdg.VigaFontFamily
import com.example.almaware.utils.generateCardById
import org.koin.androidx.compose.koinViewModel

@Composable
fun BadgeDetailScreen(
    navController: NavController,
    badge: Badge,
    authViewModel: AuthViewModel = koinViewModel(),
    badgeDetailViewModel: BadgeDetailViewModel = koinViewModel()
) {
    val badgeId = badgeDetailViewModel.getBadgeIdByName(badge.badgeName)
    val card = badgeId?.let { generateCardById(badge.sdgRef) }
    val currentUser by badgeDetailViewModel.getCurrentUserFlow().collectAsState()

    var showQuiz by remember { mutableStateOf(false) }
    var showCompletionPopup by remember { mutableStateOf(false) }
    var previousProgress by remember { mutableStateOf<Any?>(null) }

    // Monitora i cambiamenti nel progresso per mostrare il popup di completamento
    LaunchedEffect(currentUser, badgeId) {
        if (badgeId != null && currentUser != null) {
            val currentProgress = badgeDetailViewModel.getBadgeProgress(badgeId)

            // Se il progresso è cambiato e il badge è ora completato
            if (previousProgress != currentProgress &&
                badgeDetailViewModel.shouldShowCompletionPopup(badgeId)) {
                showCompletionPopup = true
            }

            previousProgress = currentProgress
        }
    }

    Scaffold(
        topBar = {
            AppBar("Prove", navController, authViewModel)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(18.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    model = badge.bubble,
                    contentDescription = badge.badgeName,
                    modifier = Modifier.size(250.dp)
                )
            }

            item {
                Spacer(Modifier.height(32.dp))
            }

            item {
                Text(
                    text = badge.badgeName,
                    fontFamily = VigaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            item {
                Spacer(Modifier.height(12.dp))
            }

            item {
                Text(
                    text = badge.description,
                    fontFamily = VigaFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

            if (badge.type == "MultiCheckbox" && badge.targetCount != null && badgeId != null) {
                item {
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Spacer(Modifier.height(24.dp))
                }

                item {
                    val badgeIndex = badgeId.toIntOrNull()?.minus(1) ?: 0
                    val currentProgress = (currentUser?.badgeProgress?.getOrNull(badgeIndex) as? Number)?.toInt() ?: 0
                    val targetCount = badge.targetCount

                    val correctBadge = badgeDetailViewModel.getBadgeById(badgeId) ?: badge

                    val progressImages = buildList {
                        repeat(currentProgress.coerceAtMost(targetCount)) {
                            add(correctBadge.done)
                        }
                        repeat(targetCount - currentProgress) {
                            add(correctBadge.notDone)
                        }
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        progressImages.chunked(5).forEach { rowImages ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                rowImages.forEach { imageUrl ->
                                    if (imageUrl.isNotEmpty()) {
                                        AsyncImage(
                                            model = imageUrl,
                                            contentDescription = "Progress indicator",
                                            modifier = Modifier.size(60.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(32.dp))
                }

                if (card != null) {
                    item {
                        val isCompleted = badgeDetailViewModel.isMultiCheckboxBadgeCompleted(badgeId)

                        Button(
                            onClick = {
                                if (!isCompleted) {
                                    badgeDetailViewModel.incrementBadgeProgress(badgeId)
                                }
                            },
                            enabled = !isCompleted,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(horizontal = 70.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCompleted) Color.Gray else Color(card.borderColor),
                                disabledContainerColor = Color.Gray
                            )
                        ) {
                            Text(
                                text = if (isCompleted) "Completed!" else "Add progress",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = VigaFontFamily
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(32.dp))
                }
            } else {
                item {
                    Spacer(Modifier.height(40.dp))
                }

                item {
                    if (card != null) {
                        val isCompleted = badgeDetailViewModel.isQuizBadgeCompleted(badgeId)

                        Button(
                            onClick = {
                                if (!isCompleted) {
                                    val badgeQuestions = badgeDetailViewModel.getBadgeById(badgeId)?.questions
                                    if (!badgeQuestions.isNullOrEmpty()) {
                                        showQuiz = true
                                    }
                                }
                            },
                            enabled = !isCompleted,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(horizontal = 70.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCompleted) Color.Gray else Color(card.borderColor),
                                disabledContainerColor = Color.Gray
                            )
                        ) {
                            Text(
                                text = if (isCompleted) "Completed!" else "Challenge yourself!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = VigaFontFamily
                            )
                        }
                    }
                }
            }
        }
    }

    // Quiz Popup
    if (showQuiz && badgeId != null) {
        val badgeQuestions = badgeDetailViewModel.getBadgeById(badgeId)?.questions ?: emptyList()
        QuizPopup(
            questions = badgeQuestions,
            onDismiss = { showQuiz = false },
            onQuizComplete = { answers ->
                badgeDetailViewModel.saveBadgeQuizAnswers(badgeId, answers)
                showQuiz = false

                // Controlla se il badge è completato per mostrare il popup
                if (badgeDetailViewModel.isQuizBadgeCompleted(badgeId)) {
                    showCompletionPopup = true
                }
            },
            cardColor = Color(card?.borderColor ?: 0xFF000000)
        )
    }

    // Badge Completion Popup
    if (showCompletionPopup && badgeId != null) {
        val completedBadge = badgeDetailViewModel.getBadgeById(badgeId)
        if (completedBadge != null) {
            BadgeCompletionPopup(
                badgeImageUrl = completedBadge.validate,
                badgeName = completedBadge.badgeName,
                cardColor = Color(card?.borderColor ?: 0xFF000000),
                onDismiss = { showCompletionPopup = false }
            )
        }
    }
}