package com.example.almaware.ui.screens.flower

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.almaware.R
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import com.example.almaware.ui.screens.sdg.VigaFontFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun FlowerScreen(
    navController: NavController,
    viewModel: FlowerViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { AppBar("Prove", navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                // Sezione superiore con decorazioni e vaso
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp),
                ) {
                    // Decorazione superiore
                    Image(
                        painter = painterResource(id = R.drawable.group36),
                        contentDescription = "Decorazione superiore sx",
                        modifier = Modifier
                            .size(250.dp)
                            .align(Alignment.TopStart),
                        contentScale = ContentScale.Crop
                    )

                    // Linea decorativa
                    Image(
                        painter = painterResource(id = R.drawable.line),
                        contentDescription = "Line",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 270.dp)
                    )

                    // Titolo
                    Text(
                        text = "My flower",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 40.dp),
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = VigaFontFamily,
                        color = Color.Black,
                        letterSpacing = (-0.3).sp
                    )

                    // Pianta - mostra quella selezionata o default (PRIMA del vaso)
                    val selectedPlant = uiState.availablePlants.find { it.id == uiState.selectedFlowerType }

                    if (selectedPlant != null) {
                        SubcomposeAsyncImage(
                            model = selectedPlant.imageUrl,
                            contentDescription = "Selected Plant",
                            modifier = Modifier
                                .size(110.dp)
                                .align(Alignment.Center),
                            contentScale = ContentScale.Fit,
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp
                                    )
                                }
                            },
                            error = {
                                Image(
                                    painter = painterResource(id = R.drawable.plant),
                                    contentDescription = "Default Plant",
                                    modifier = Modifier.size(110.dp)
                                )
                            }
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.plant),
                            contentDescription = "Default Plant",
                            modifier = Modifier
                                .size(110.dp)
                                .align(Alignment.Center),
                        )
                    }

                    // Vaso con colore dinamico (DOPO la pianta per coprirla)
                    Image(
                        painter = painterResource(id = R.drawable.pot),
                        contentDescription = "Pot",
                        modifier = Modifier
                            .size(135.dp)
                            .offset(y = 88.dp)
                            .align(Alignment.Center),
                        colorFilter = when(uiState.selectedColorIndex) {
                            -1 -> ColorFilter.tint(Color(0xFFDD1367L)) // Colore default
                            in 0..16 -> ColorFilter.tint(Color(viewModel.potColors[uiState.selectedColorIndex]))
                            17, 18 -> null // Per i pattern multicolore
                            else -> ColorFilter.tint(Color(0xFFDD1367L))
                        }
                    )
                }

                Spacer(Modifier.height(5.dp))

                // Sezione scrollabile con le opzioni
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF0EDE8))
                        .verticalScroll(rememberScrollState())
                ) {
                    Column {
                        // SEZIONE NOME FIORE
                        Text(
                            text = "Flower name",
                            modifier = Modifier
                                .padding(
                                    start = 18.dp,
                                    top = 18.dp,
                                ),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = VigaFontFamily,
                            color = Color.Black,
                            letterSpacing = (-0.3).sp
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = uiState.flowerName,
                                onValueChange = { viewModel.updateFlowerName(it) },
                                label = {
                                    Text(
                                        text = "Flower name",
                                        fontFamily = VigaFontFamily
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = "Name",
                                        fontFamily = VigaFontFamily
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(28.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.White,
                                    focusedLabelColor = Color.Black,
                                    unfocusedLabelColor = Color.Black,
                                    cursorColor = Color.Black,
                                    focusedContainerColor = Color(0xFFF0EDE8),
                                    unfocusedContainerColor = Color(0xFFF0EDE8),
                                    focusedPlaceholderColor = Color.Gray,
                                    unfocusedPlaceholderColor = Color.Gray
                                ),
                                singleLine = true,
                                trailingIcon = {
                                    when {
                                        uiState.isSavingName -> {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                strokeWidth = 2.dp
                                            )
                                        }
                                        uiState.nameSaved -> {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Saved",
                                                tint = Color.Green,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        // SEZIONE COLORE VASO
                        Text(
                            text = "Flower pot color",
                            modifier = Modifier
                                .padding(
                                    start = 18.dp,
                                    top = 18.dp,
                                ),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = VigaFontFamily,
                            color = Color.Black,
                            letterSpacing = (-0.3).sp
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 12.dp)
                        ) {
                            // Griglia dei colori SDG
                            viewModel.potColors.chunked(6).forEachIndexed { rowIndex, rowColors ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    rowColors.forEachIndexed { colIndex, color ->
                                        val index = rowIndex * 6 + colIndex
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .padding(4.dp)
                                                .background(
                                                    color = Color(color),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .border(
                                                    width = if (uiState.selectedColorIndex == index) 3.dp else 0.dp,
                                                    color = Color.Black,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
                                                    viewModel.selectColor(index)
                                                }
                                        )
                                    }
                                }
                            }

                            // Pattern multicolore
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                // Pattern multicolore 1 (gradiente lineare)
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .padding(4.dp)
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFFFF0000),
                                                    Color(0xFFFFFF00),
                                                    Color(0xFF00FF00),
                                                    Color(0xFF0000FF),
                                                    Color(0xFFFF00FF)
                                                )
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            width = if (uiState.selectedColorIndex == 17) 3.dp else 0.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            viewModel.selectColor(17)
                                        }
                                )

                                // Pattern multicolore 2 (gradiente verticale)
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .padding(4.dp)
                                        .background(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Color(0xFFFF0000),
                                                    Color(0xFFFFA500),
                                                    Color(0xFFFFFF00),
                                                    Color(0xFF00FF00),
                                                    Color(0xFF0000FF)
                                                )
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            width = if (uiState.selectedColorIndex == 18) 3.dp else 0.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            viewModel.selectColor(18)
                                        }
                                )
                            }
                        }

                        // SEZIONE TIPO DI PIANTA
                        Text(
                            text = "Flower type",
                            modifier = Modifier
                                .padding(
                                    start = 18.dp,
                                    top = 18.dp,
                                ),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = VigaFontFamily,
                            color = Color.Black,
                            letterSpacing = (-0.3).sp
                        )

                        if (uiState.plantsLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else if (uiState.availablePlants.isEmpty()) {
                            // Mostra messaggio se non ci sono piante
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .padding(horizontal = 18.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Nessuna pianta disponibile. Controlla la connessione o Firebase Storage.",
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    color = Color.Gray,
                                    fontFamily = VigaFontFamily
                                )
                            }
                        } else {
                            LazyRow(
                                modifier = Modifier
                                    .padding(horizontal = 18.dp, vertical = 12.dp)
                            ) {
                                items(uiState.availablePlants) { plant ->
                                    Card(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(4.dp)
                                            .clickable {
                                                viewModel.selectFlowerType(plant.id)
                                            },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (uiState.selectedFlowerType == plant.id)
                                                Color(0xFF4CAF50).copy(alpha = 0.2f) // Verde trasparente quando selezionato
                                            else
                                                Color.White
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = if (uiState.selectedFlowerType == plant.id) 8.dp else 2.dp
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp)
                                        ) {
                                            SubcomposeAsyncImage(
                                                model = plant.imageUrl,
                                                contentDescription = "Plant ${plant.name}",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(8.dp)),
                                                contentScale = ContentScale.Fit,
                                                loading = {
                                                    Box(
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        CircularProgressIndicator(
                                                            modifier = Modifier.size(24.dp),
                                                            strokeWidth = 2.dp
                                                        )
                                                    }
                                                },
                                                error = {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.plant),
                                                        contentDescription = "Plant",
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentScale = ContentScale.Fit
                                                    )
                                                }
                                            )

                                            // Indicatore di selezione
                                            if (uiState.selectedFlowerType == plant.id) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Selected",
                                                    tint = Color(0xFF4CAF50),
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .align(Alignment.TopEnd)
                                                        .background(
                                                            Color.White,
                                                            shape = CircleShape
                                                        )
                                                        .padding(2.dp)
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
        }
    }
}