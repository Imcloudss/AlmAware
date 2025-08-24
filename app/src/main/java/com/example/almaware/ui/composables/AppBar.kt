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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.almaware.data.model.User
import com.example.almaware.data.repository.AuthRepository
import com.example.almaware.ui.theme.AlmAwareRoute
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    userName: String = "Default name",
    navController: NavController
) {
    var currentUser by remember { mutableStateOf<User?>(null) }

    val authRepository = remember {
        AuthRepository(
            auth = FirebaseAuth.getInstance(),
            db = FirebaseDatabase.getInstance().reference
        )
    }

    // Recupera l'utente corrente quando l'AppBar viene composta
    LaunchedEffect(Unit) {
        authRepository.getCurrentUser { user ->
            currentUser = user
        }
    }

    // Usa l'username dell'utente o "Guest" come fallback
    val displayName = currentUser?.username ?: userName

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
                    authRepository.signOut()
                    navController.navigate(AlmAwareRoute.Authentication)
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