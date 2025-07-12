package com.example.almaware.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.almaware.R
import com.example.almaware.ui.theme.AlmAwareRoute

@Composable
fun BottomNavigationBar(navController: NavController) {
    Column {
        Image(
            painter = painterResource(id = R.drawable.line),
            contentDescription = "Linea decorativa",
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp),
            contentScale = ContentScale.FillBounds
        )

        NavigationBar(
            containerColor = Color(0xFFF0EDE8)
        ) {
            // Home
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(AlmAwareRoute.Home) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.homeicon), // Un solo drawable
                        contentDescription = "Home",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(30.dp)
                    )
                },
            )

            // Profile
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(AlmAwareRoute.Profile) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.profilicon), // Un solo drawable
                        contentDescription = "Profile",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(30.dp)
                    )
                },
            )

            // Awards
            NavigationBarItem(
                selected = false,
                onClick = { /*navController.navigate(AlmAwareRoute.Awards)*/},
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.badgeicon), // Un solo drawable
                        contentDescription = "Awards",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(30.dp)
                    )
                },
            )
        }
    }
}