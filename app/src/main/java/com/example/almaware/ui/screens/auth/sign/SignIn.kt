package com.example.almaware.ui.screens.auth.sign

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.almaware.R
import com.example.almaware.ui.theme.AlmAwareRoute

@Composable
fun SignInScreen(
    navController: NavHostController,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.group38_1),
            contentDescription = "Decorazione superiore",
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .size(300.dp),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.group43),
            contentDescription = "Decorazione inferiore",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .size(200.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.35f))

            Text(
                text = "Welcome Back",
                modifier = Modifier.padding(horizontal = 40.dp),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black,
                letterSpacing = (-0.3).sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.42f))

            // Text Field per inserire l'email
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = "email",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 30.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(60),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFF0EDE8),  // Bordo grigio chiaro
                    unfocusedIndicatorColor = Color(0xFFF0EDE8),  // Bordo grigio chiaro
                    focusedContainerColor = Color.White,  // Interno bianco
                    unfocusedContainerColor = Color.White  // Interno bianco
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.03f))

            // Text Field per inserire la password
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = "Password",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 30.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(60),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFF0EDE8),  // Bordo grigio chiaro
                    unfocusedIndicatorColor = Color(0xFFF0EDE8),  // Bordo grigio chiaro
                    focusedContainerColor = Color.White,  // Interno bianco
                    unfocusedContainerColor = Color.White  // Interno bianco
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.04f))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append("Forgotten password?")
                    }
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier
                    .clickable { /* Apri policies */ }
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.11f))

            Button(
                onClick = {
                    // Fare controllo una volta che si ha anche il db
                    navController.navigate(AlmAwareRoute.Home)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 90.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
                )
            ) {
                Text(
                    text = "Sign in",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("No account yet? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Sign up")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(AlmAwareRoute.SignUp) },
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
            )
        }
    }
}