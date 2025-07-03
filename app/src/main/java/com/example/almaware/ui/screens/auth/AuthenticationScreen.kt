package com.example.almaware.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.almaware.R
import com.example.almaware.ui.theme.AlmAwareRoute

@Composable
fun AuthenticationScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Box superiore bianco (70% dell'altezza)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .background(Color.White)
        ) {
            // Gruppo in alto a sinistra
            Image(
                painter = painterResource(id = R.drawable.group41),
                contentDescription = "Decorazione superiore",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .size(234.dp),
                contentScale = ContentScale.Crop
            )

            Column {
                Spacer(modifier = Modifier.fillMaxHeight(0.25f))

                // Titolo "AlmAware"
                Image(
                    painter = painterResource(id = R.drawable.title),
                    contentDescription = "AlmAware",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.fillMaxHeight(0.07f))

                // Mascotte AlmAware
                Image(
                    painter = painterResource(id = R.drawable.mascotte),
                    contentDescription = "Mascotte AlmAware",
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(400.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Linea colorata separatrice
        Image(
            painter = painterResource(id = R.drawable.line),
            contentDescription = "Linea decorativa",
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp),
            contentScale = ContentScale.FillBounds
        )

        // Box inferiore beige (30% dell'altezza)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .background(Color(0xFFF0EDE8))
        ) {
            // Pulsanti centrati nel box inferiore
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.15f))

                Button(
                    onClick = { navController.navigate(AlmAwareRoute.SignIn) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text(
                        text = "Sign in",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.fillMaxHeight(0.1f))

                OutlinedButton(
                    onClick = {
                        navController.navigate(AlmAwareRoute.SignUp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F) // Rosso per testo
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = Color(0xFFD32F2F)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Sign up",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.group40),
                contentDescription = "Decorazione inferiore",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .offset(x = 15.dp),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}