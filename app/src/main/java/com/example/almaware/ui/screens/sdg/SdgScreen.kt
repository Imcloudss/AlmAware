package com.example.almaware.ui.screens.sdg

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import com.example.almaware.ui.screens.home.HomeCard

@Composable
fun SdgScreen(
    navController: NavController,
    item: HomeCard
) {
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
                        .graphicsLayer { alpha = 0.5f }
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
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut et massa mi. Aliquam in hendrerit urna. Pellentesque sit amet sapien fringilla, mattis ligula consectetur, ultrices mauris. Maecenas vitae mattis tellus. Nullam quis imperdiet augue. ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
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
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(2.dp, Color(item.borderColor)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(item.borderColor)
                    )
                ) {
                    Text(
                        text = "Unibo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Button Student (filled)
                Button(
                    onClick = { /* Azione per Student */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(item.borderColor),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Student",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}