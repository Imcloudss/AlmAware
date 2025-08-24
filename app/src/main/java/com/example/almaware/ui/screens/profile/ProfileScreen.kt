package com.example.almaware.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almaware.R
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.BottomNavigationBar
import com.example.almaware.ui.theme.AlmAwareRoute

@Composable
fun ProfileScreen(
    navController: NavController
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
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.group38_1),
                contentDescription = "Decorazione superiore",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(300.dp)
                    .offset(y = (-10).dp),
                colorFilter = ColorFilter.colorMatrix(
                    ColorMatrix().apply {
                        setToSaturation(0f)
                    }
                ),
                contentScale = ContentScale.Crop
            )

            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center)
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.32f))

                Text(
                    text = "My account",
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

                // Text Field per inserire il nome
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "Name",
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

                // Text Field per inserire il campus
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "Campus",
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

                Spacer(modifier = Modifier.fillMaxHeight(0.03f))



                Spacer(modifier = Modifier.fillMaxHeight(0.2f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(AlmAwareRoute.Flower) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF0EDE8),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "My flower",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Button Student (filled)
                    Button(
                        onClick = { navController.navigate(AlmAwareRoute.Awards) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF0EDE8),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "My badges",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}