package com.example.almaware.ui.screens.sdg

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.almaware.R
import com.example.almaware.data.model.HomeCard
import com.example.almaware.data.model.SDG
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.compose.koinViewModel

val VigaFontFamily = FontFamily(
    Font(R.font.viga)
)

@Composable
fun SdgScreen(
    navController: NavController,
    item: HomeCard,
    viewModel: SdgViewModel = koinViewModel()
) {
    val sdg = viewModel.sdg.value

    LaunchedEffect(Unit) {
        viewModel.loadSdgById(item.id)
    }

    var imageUrl by remember { mutableStateOf<String?>(null) }
    var backgroundUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(item.id) {
        val storage = FirebaseStorage.getInstance()

        storage.reference.child("cards/card${item.id}.png")
            .downloadUrl
            .addOnSuccessListener { uri ->
                imageUrl = uri.toString()
            }

        storage.reference.child("backgrounds/sfondo${item.id}.png")
            .downloadUrl
            .addOnSuccessListener { uri ->
                backgroundUrl = uri.toString()
            }
    }

    Scaffold(
        topBar = {
            AppBar("Prove", navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                sdg?.background?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "Card SDG ${item.id}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .alpha(0.7f)
                    )
                }

                Text(
                    text = sdg?.title ?: "Caricamento...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .offset(y = (-30).dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(item.borderColor),
                    textAlign = TextAlign.Center,
                    fontFamily = VigaFontFamily
                )
            }

            Image(
                painter = painterResource(id = R.drawable.line),
                contentDescription = "Line",
                contentScale = ContentScale.Crop
            )

            sdg?.image?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "Card SDG ${item.id}",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset(y = (-90).dp)
                        .size(150.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Log.d("ImageUrl", "ImageUrl: $imageUrl")

            Text(
                text = sdg?.subtitle ?: "Caricamento descrizione...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-60).dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontFamily = VigaFontFamily
            )

            if (sdg != null) {
                DescriptionCards(
                    cardColor = Color(item.borderColor),
                    sdg = sdg
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("unibo/${item.id}") },
                    modifier = Modifier
                        .weight(1f)
                        .height(IntrinsicSize.Min),
                    border = BorderStroke(2.dp, Color(item.borderColor)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(item.borderColor)
                    )
                ) {
                    Text(
                        text = "What does Unibo do?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Button(
                    onClick = { navController.navigate("student/${item.id}") },
                    modifier = Modifier
                        .weight(1f)
                        .height(IntrinsicSize.Min),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(item.borderColor),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "What can we do?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DescriptionCards(
    numberOfCards: Int = 3,
    cardColor: Color,
    spacing: Int = 8,
    paddingHorizontal: Int = 16,
    sdg: SDG
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = paddingHorizontal.dp)
            .offset(y = (-25).dp),
        horizontalArrangement = Arrangement.spacedBy(spacing.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(numberOfCards) { index ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = sdg.description[index].number,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontFamily = VigaFontFamily
                    )

                    if(sdg.description[index].unit.isNotEmpty()) {
                        Text(
                            text = sdg.description[index].unit,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontFamily = VigaFontFamily,
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                                .offset(y = (-6).dp)
                        )
                    }
                    val modifierText: Modifier = if(sdg.description[index].unit.isNotEmpty()) {
                        Modifier
                            .padding(start = 3.dp, end = 3.dp, bottom = 5.dp)
                            .offset(y = (-9).dp)
                    } else {
                        Modifier
                            .padding(start = 3.dp, end = 3.dp, bottom = 5.dp)
                            .offset(y = (-2).dp)
                    }
                    Text(
                        text = sdg.description[index].text,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontFamily = VigaFontFamily,
                        modifier = modifierText,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}