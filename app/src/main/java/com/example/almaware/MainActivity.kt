package com.example.almaware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.almaware.ui.theme.AlmAwareNavigation
import com.example.almaware.ui.theme.AlmAwareTheme
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import com.example.almaware.di.appModule


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        setContent {
            AlmAwareTheme {
                val navController = rememberNavController()
                AlmAwareNavigation(navController)
            }
        }
    }
}
