package com.example.almaware.ui.screens.auth.sign

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.almaware.ui.screens.auth.AuthState
import com.example.almaware.ui.screens.auth.AuthViewModel
import com.example.almaware.ui.theme.AlmAwareRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = koinViewModel()
) {
    var confirmPassword by remember { mutableStateOf("") }

    val authState = viewModel.authState

    // Navigazione automatica su successo
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate(AlmAwareRoute.Home) {
                popUpTo(AlmAwareRoute.SignUp) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Image(
            painter = painterResource(id = R.drawable.group39),
            contentDescription = "Decorazione superiore",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(300.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = R.drawable.group38),
            contentDescription = "Decorazione inferiore",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(350.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.19f))

            Text(
                text = "Create account",
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
            Spacer(modifier = Modifier.fillMaxHeight(0.26f))

            // Name
            OutlinedTextField(
                value = viewModel.name,
                label = { Text("Username") },
                onValueChange = { viewModel.name = it },
                placeholder = { Text("Username", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(60),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFF0EDE8),
                    unfocusedIndicatorColor = Color(0xFFF0EDE8),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.03f))

            // Email
            OutlinedTextField(
                value = viewModel.email,
                label = { Text("Email") },
                onValueChange = { viewModel.email = it },
                placeholder = { Text("Email", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(60),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFF0EDE8),
                    unfocusedIndicatorColor = Color(0xFFF0EDE8),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.03f))

            // Password
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Password") },
                placeholder = { Text("Password", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(60),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFF0EDE8),
                    unfocusedIndicatorColor = Color(0xFFF0EDE8),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.03f))

            // Confirm password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm password") },
                placeholder = { Text("Confirm password", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(60),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFF0EDE8),
                    unfocusedIndicatorColor = Color(0xFFF0EDE8),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.04f))

            // Check policies
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 35.dp)
            ) {
                Checkbox(
                    checked = viewModel.acceptPolicies,
                    onCheckedChange = { viewModel.acceptPolicies = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFD32F2F),
                        uncheckedColor = Color.Gray
                    ),
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("I accept all the policies and terms")
                        }
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { /* Apri policies */ }
                        .padding(vertical = 12.dp)
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.11f))

            // SIGN UP button
            Button(
                onClick = {
                    if (viewModel.acceptPolicies && viewModel.password == confirmPassword) {
                        viewModel.signUp()
                    }
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
                    text = if (authState is AuthState.Loading) "Loading..." else "Sign up",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Messaggi di errore
            if (authState is AuthState.Error) {
                Text(
                    text = authState.message,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            // Already have account? Sign In
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Already have an account? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Sign In")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(AlmAwareRoute.SignIn) },
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
            )
        }
    }
}