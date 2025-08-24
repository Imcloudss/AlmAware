package com.example.almaware.ui.screens.sdg.unibo.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almaware.data.model.HomeCard
import com.example.almaware.ui.composables.AppBar
import com.example.almaware.ui.composables.ComposeBubble
import com.example.almaware.ui.screens.sdg.VigaFontFamily

@Composable
fun ClickableSdgScreen(
    navController: NavController,
    item: HomeCard,
    string: String,
    value: Int
) {
    Scaffold(
        topBar = {
            AppBar(
                "Prove",
                navController
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ComposeBubble(
                    item,
                    string
                )

                Text(
                    text = value.toString(),
                    fontFamily = VigaFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
            }
        }
    }
}
