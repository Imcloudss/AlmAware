package com.example.almaware.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import com.example.almaware.ui.screens.home.data.backgrounds
import com.example.almaware.ui.screens.home.data.borderColors
import com.example.almaware.ui.screens.home.data.cardOverlays

// Data class per le card
data class HomeCard(
    val id: Int,
    val backgroundRes: Int,
    val overlayRes: Int,
    val borderColor: Color,
    val route: String,
)

@Composable
fun HomeScreen(
    navController: NavController
) {
    val items = (0 until 17).map { index ->
        HomeCard(
            id = index + 1,
            backgroundRes = backgrounds[index],
            overlayRes = cardOverlays[index],
            borderColor = borderColors[index],
            route = "destination${index + 1}"
        )
    }

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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
            modifier =  Modifier.padding(contentPadding)
        ) {
            items(items) { item ->
                CardItem(
                    item,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun CardItem(
    item: HomeCard,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .height(270.dp)
            .width(100.dp)
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = item.borderColor,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = item.backgroundRes),
                contentDescription = "Sfondo card${item.id}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { alpha = 0.5f }
            )

            Image(
                painter = painterResource(id= item.overlayRes),
                contentDescription = "Card${item.id}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .align(Alignment.Center)
            )
        }
    }
}
