package com.example.almaware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.almaware.ui.theme.AlmAwareNavigation
import com.example.almaware.ui.theme.AlmAwareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlmAwareTheme {
                val navController = rememberNavController()
                AlmAwareNavigation(navController)
            }
        }
    }
}
