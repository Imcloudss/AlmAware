package com.example.almaware.ui.screens.sdg

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almaware.R
import com.example.almaware.data.model.HomeCard
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SdgScreen(
    navController: NavController,
    item: HomeCard,
    viewModel: SdgViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadSdgById(item.id)
    }

    val sdg = viewModel.sdg.value

    Scaffold(
        topBar = {
            AppBar(
                "Prove",
                navController
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Image(
                    painter = painterResource(id = item.backgroundRes),
                    contentDescription = "Background card${item.id}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer { alpha = 0.45f }
                )

                Text(
                    text = sdg?.title ?: "Caricamento...",
                    modifier = Modifier
                        .fillMaxWidth()  // Aggiungi questo se necessario
                        .align(Alignment.Center)
                        .offset(y = (-30).dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(item.borderColor),
                    textAlign = TextAlign.Center
                )
            }

            Image(
                painter = painterResource(id = R.drawable.line),
                contentDescription = "Line",
                contentScale = ContentScale.Crop
            )

            Image(
                painter = painterResource(id = item.overlayRes),
                contentDescription = "Card${item.id}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .offset(y= (-90).dp)
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.01f))

            Text(
                text = sdg?.subtitle ?: "Caricamento descrizione...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.3f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Button Unibo (outlined)
                OutlinedButton(
                    onClick = { /* Azione per Unibo */ },
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

                // Button Student (filled)
                Button(
                    onClick = { /* Azione per Student */ },
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