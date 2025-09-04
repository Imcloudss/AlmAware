package com.example.almaware.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.almaware.data.model.QuizQuestion
import com.example.almaware.ui.screens.sdg.VigaFontFamily

@SuppressLint("MutableCollectionMutableState")
@Composable
fun QuizPopup(
    questions: List<QuizQuestion>,
    onDismiss: () -> Unit,
    onQuizComplete: (List<String>) -> Unit,
    cardColor: Color
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, String>()) }
    var currentSelectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResults by remember { mutableStateOf(false) }
    var quizScore by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentQuestionIndex) {
        currentSelectedAnswer = selectedAnswers[currentQuestionIndex]
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    val isLastQuestion = currentQuestionIndex == questions.size - 1
    val canGoNext = currentSelectedAnswer != null
    val canGoPrevious = currentQuestionIndex > 0

    // Calcola il punteggio
    fun calculateScore(): Int {
        var correct = 0
        questions.forEachIndexed { index, question ->
            val userAnswer = selectedAnswers[index] ?: ""
            val correctAnswer = when (question.correctAnswer) {
                1 -> question.answer1
                2 -> question.answer2
                3 -> question.answer3
                4 -> question.answer4
                else -> question.answer1
            }
            if (userAnswer == correctAnswer) {
                correct++
            }
        }
        return (correct * 100) / questions.size
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                if (showResults) {
                    // Schermata dei risultati
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Quiz Results",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = VigaFontFamily,
                            color = cardColor
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "$quizScore%",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (quizScore >= 80) Color(0xFF4CAF50) else Color(0xFFf44336)
                        )

                        Text(
                            text = if (quizScore >= 80) "Congratulations!" else "Try Again",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (quizScore >= 80) Color(0xFF4CAF50) else Color(0xFFf44336),
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = if (quizScore >= 80)
                                "You passed the quiz! Badge earned!"
                            else
                                "You need at least 80% to earn the badge",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (quizScore < 80) {
                                Button(
                                    onClick = {
                                        // Reset quiz
                                        currentQuestionIndex = 0
                                        selectedAnswers.clear()
                                        currentSelectedAnswer = null
                                        showResults = false
                                        quizScore = 0
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = cardColor
                                    ),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text(
                                        "Try Again",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        )
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    if (quizScore >= 80) {
                                        val finalAnswers = mutableListOf<String>()
                                        for (i in questions.indices) {
                                            finalAnswers.add(selectedAnswers[i] ?: "")
                                        }
                                        onQuizComplete(finalAnswers)
                                    }
                                    onDismiss()
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = cardColor
                                ),
                                shape = RoundedCornerShape(25.dp)
                            ) {
                                Text(
                                    if (quizScore >= 80) "Complete" else "Close",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                } else {
                    // Schermata delle domande (codice esistente)
                    // Header con pulsante chiudi e progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Question ${currentQuestionIndex + 1}/${questions.size}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )

                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close quiz",
                                tint = Color.Gray
                            )
                        }
                    }

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { (currentQuestionIndex + 1).toFloat() / questions.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        color = cardColor,
                        trackColor = Color.Gray.copy(alpha = 0.3f),
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    currentQuestion?.let { question ->
                        // Domanda
                        Text(
                            text = question.question,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = VigaFontFamily,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Opzioni di risposta
                        val answers = listOf(
                            question.answer1,
                            question.answer2,
                            question.answer3,
                            question.answer4
                        )

                        answers.forEach { answer ->
                            AnswerOption(
                                answer = answer,
                                isSelected = currentSelectedAnswer == answer,
                                cardColor = cardColor,
                                onClick = {
                                    currentSelectedAnswer = answer
                                    selectedAnswers[currentQuestionIndex] = answer
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Navigation buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous button
                        if (canGoPrevious) {
                            OutlinedButton(
                                onClick = { currentQuestionIndex-- },
                                modifier = Modifier.size(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = cardColor
                                )
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Previous question"
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(50.dp))
                        }

                        // Next/Finish button
                        Button(
                            onClick = {
                                if (isLastQuestion) {
                                    // Calcola punteggio e mostra risultati
                                    quizScore = calculateScore()
                                    showResults = true
                                } else {
                                    currentQuestionIndex++
                                }
                            },
                            enabled = canGoNext,
                            modifier = Modifier.height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cardColor,
                                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                            )
                        ) {
                            if (isLastQuestion) {
                                Text(
                                    "Finish Quiz",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Next",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Next question",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerOption(
    answer: String,
    isSelected: Boolean,
    cardColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) cardColor.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.05f)
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) cardColor else Color.Transparent
        )
    ) {
        Text(
            text = answer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = if (isSelected) cardColor else Color.Black,
            textAlign = TextAlign.Center
        )
    }
}