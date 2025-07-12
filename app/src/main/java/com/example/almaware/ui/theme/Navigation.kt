package com.example.almaware.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.almaware.ui.screens.auth.AuthenticationScreen
import com.example.almaware.ui.screens.auth.sign.SignInScreen
import com.example.almaware.ui.screens.auth.sign.SignUpScreen
import com.example.almaware.ui.screens.home.HomeScreen
import com.example.almaware.ui.screens.profile.ProfileScreen
import com.example.almaware.ui.screens.sdg.SdgScreen
import com.example.almaware.ui.screens.splash.SplashScreen
import com.example.almaware.utils.generateCardById
import kotlinx.serialization.Serializable

// Navigation Routes
sealed interface AlmAwareRoute {
    @Serializable data object Splash : AlmAwareRoute
    @Serializable data object Authentication : AlmAwareRoute
    @Serializable data object SignIn : AlmAwareRoute
    @Serializable data object SignUp : AlmAwareRoute
    @Serializable data object Home : AlmAwareRoute
    @Serializable data object Profile : AlmAwareRoute
}

// Main Navigation Graph
@Composable
fun AlmAwareNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AlmAwareRoute.Splash
    ) {
        // Splash Screen
        composable<AlmAwareRoute.Splash> {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(AlmAwareRoute.Authentication) {
                        popUpTo(AlmAwareRoute.Splash) { inclusive = true }
                    }
                }
            )
        }

        // Authentication Screens
        composable<AlmAwareRoute.Authentication> {
            AuthenticationScreen(navController)
        }
        composable<AlmAwareRoute.SignUp> {
            SignUpScreen(navController)
        }
        composable<AlmAwareRoute.SignIn> {
            SignInScreen(navController)
        }

        // Main App Screens
        composable<AlmAwareRoute.Home> {
            HomeScreen(navController)
        }
        composable("cardDetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                val card = generateCardById(id)
                SdgScreen(
                    navController,
                    card
                )
            }
        }
        composable<AlmAwareRoute.Profile> {
            ProfileScreen(navController)
        }
    }
}

// Main App Navigation Wrapper
@Composable
fun AlmAwareNavigation(
    navController: NavHostController = rememberNavController()
) {
    AlmAwareNavGraph(navController = navController)
}


