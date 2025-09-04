package com.example.almaware.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.almaware.ui.screens.sdg.VigaFontFamily
import kotlinx.coroutines.delay

@Composable
fun BadgeCompletionPopup(
    badgeImageUrl: String,
    badgeName: String,
    cardColor: Color,
    onDismiss: () -> Unit
) {
    var showConfetti by remember { mutableStateOf(false) }

    // Avvia i coriandoli dopo un breve delay
    LaunchedEffect(Unit) {
        delay(300)
        showConfetti = true
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .padding(16.dp)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Titolo congratulazioni
                    Text(
                        text = "Congratulations!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = VigaFontFamily,
                        color = cardColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "You earned a new badge!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Container per l'immagine del badge con coriandoli
                    Box(
                        modifier = Modifier.size(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Immagine del badge
                        AsyncImage(
                            model = badgeImageUrl,
                            contentDescription = "Completed badge: $badgeName",
                            modifier = Modifier.size(180.dp)
                        )

                        // Animazione coriandoli sovrapposta
                        if (showConfetti) {
                            ConfettiAnimation(
                                modifier = Modifier.fillMaxSize(),
                                isVisible = showConfetti,
                                particleCount = 60
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nome del badge
                    Text(
                        text = badgeName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = VigaFontFamily,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Button per aggiungere ai badge
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cardColor
                        )
                    ) {
                        Text(
                            text = "Add to Your Badges",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = VigaFontFamily
                        )
                    }
                }
            }

            // Coriandoli che si estendono oltre il badge per effetto più spettacolare
            if (showConfetti) {
                ConfettiAnimation(
                    modifier = Modifier.fillMaxSize(),
                    isVisible = showConfetti,
                    particleCount = 40
                )
            }
        }
    }
}