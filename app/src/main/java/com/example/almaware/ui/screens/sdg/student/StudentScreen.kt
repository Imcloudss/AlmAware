package com.example.almaware.ui.screens.sdg.student

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.almaware.R
import com.example.almaware.data.model.HomeCard
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import com.example.almaware.ui.composables.getIconForType
import com.example.almaware.ui.screens.auth.AuthViewModel
import com.example.almaware.ui.screens.sdg.SdgViewModel
import com.example.almaware.ui.screens.sdg.VigaFontFamily
import com.example.almaware.utils.getDrawableForSdg
import org.koin.androidx.compose.koinViewModel

@Composable
fun StudentScreen(
    navController: NavController,
    item: HomeCard,
    sdgViewModel: SdgViewModel = koinViewModel(),
    badgeViewModel: StudentViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    // Carica SDG e tutti i badge al primo avvio
    LaunchedEffect(Unit) {
        sdgViewModel.loadSdgById(item.id)
        badgeViewModel.loadBadges() // Carica tutti i badge da Firebase
    }

    val sdg = sdgViewModel.sdg.value
    val advicesList = sdg?.objectives ?: emptyList()

    Log.d("AdvicesList", "AdvicesList per sdg n.${item.id}: $advicesList")

    // Otteniamo i badge filtrati direttamente dal ViewModel
    val allBadges by badgeViewModel.badges.collectAsState()
    val filteredBadges = remember(allBadges, item.id) {
        badgeViewModel.getBadgesForSdg(item.id)
    }

    Scaffold(
        topBar = { AppBar("Prove", navController, authViewModel) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(getDrawableForSdg(item.id)),
                    contentDescription = "Bubble${sdg?.id?.plus(1)}",
                    alignment = Alignment.TopStart,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset(x = 45.dp, y = (-60).dp)
                        .size(150.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.group2),
                    contentDescription = "Decorazione superiore",
                    alignment = Alignment.TopStart,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(200.dp)
                        .offset(x = (-145).dp)
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "What can we do?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        fontFamily = VigaFontFamily,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(top = 100.dp, bottom = 40.dp)
                    )
                }

                item {
                    Text(
                        text = "Advices",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        fontFamily = VigaFontFamily,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, bottom = 16.dp)
                    )
                }

                // Lista advices
                items(advicesList) { advice ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bubble),
                            contentDescription = "Bubble",
                            tint = Color(item.borderColor),
                            modifier = Modifier.size(30.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = advice,
                            fontSize = 18.sp,
                            fontFamily = VigaFontFamily,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(start = 56.dp, end = 20.dp),
                        thickness = 1.dp,
                        color = Color(item.borderColor)
                    )
                }

                // Sezione Badge
                if (allBadges.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(item.borderColor)
                            )
                        }
                    }
                } else if (filteredBadges.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = when (filteredBadges.size) {
                                1 -> Arrangement.Center
                                2 -> Arrangement.SpaceEvenly
                                else -> Arrangement.SpaceBetween
                            }
                        ) {
                            filteredBadges.forEach { badge ->
                                BadgeCardWithText(
                                    badge = badge,
                                    borderColor = item.borderColor,
                                    onClick = { navController.navigate("badge/${Uri.encode(badge.badgeName)}") }
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Composable
fun BadgeCardWithText(
    badge: com.example.almaware.data.model.Badge,
    borderColor: Long,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(borderColor))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            // Icona del badge (placeholder)
            if(badge.icon.isNotEmpty()) {
                AsyncImage(
                    model = badge.icon,
                    contentDescription = "Badge icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(85.dp)
                )
            } else {
                Icon(
                    getIconForType(badge.type),
                    contentDescription = "Badge icon",
                    tint = Color.White,
                    modifier = Modifier.size(85.dp)
                )
            }
        }

        // Testo sotto la card
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = badge.badgeName.ifEmpty { "Badge" },
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = VigaFontFamily,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}