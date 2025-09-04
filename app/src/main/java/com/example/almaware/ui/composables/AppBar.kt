package com.example.almaware.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almaware.R
import com.example.almaware.ui.screens.auth.AuthViewModel
import com.example.almaware.ui.theme.AlmAwareRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String = "",
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val currentUser = authViewModel.getUser()
    val displayName = currentUser?.username ?: title.ifEmpty { "Guest" }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Hi $displayName!",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                )
            },
            navigationIcon = {
                if (navController.previousBackStackEntry != null) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Go Back",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = {
                    authViewModel.signOut()
                    navController.navigate(AlmAwareRoute.Authentication) {
                        popUpTo(0) { inclusive = true }
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.logout),
                        contentDescription = "Log out",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFFF0EDE8)
            )
        )

        Image(
            painter = painterResource(id = R.drawable.line),
            contentDescription = "Linea decorativa",
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}