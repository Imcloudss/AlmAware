package com.example.almaware.ui.theme

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.almaware.ui.screens.sdg.badge.BadgeDetailScreen
import com.example.almaware.ui.screens.sdg.student.StudentScreen
import com.example.almaware.ui.screens.sdg.student.StudentViewModel
import com.example.almaware.ui.screens.sdg.unibo.UniboScreen
import com.example.almaware.ui.screens.splash.SplashScreen
import com.example.almaware.utils.generateCardById
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

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
        composable("unibo/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                val card = generateCardById(id)
                UniboScreen(
                    navController,
                    card
                )
            }
        }
        composable("student/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                val card = generateCardById(id)
                StudentScreen(
                    navController,
                    card
                )
            }
        }
        composable<AlmAwareRoute.Profile> {
            ProfileScreen(navController)
        }
        composable("badge/{badgeName}") { backStackEntry ->
            val badgeNameArg = backStackEntry.arguments?.getString("badgeName") ?: return@composable
            val badgeName = Uri.decode(badgeNameArg)

            val badgeViewModel: StudentViewModel = koinViewModel()
            val badges by badgeViewModel.badges.collectAsState()

            // Se la lista è vuota, la carico
            LaunchedEffect(Unit) {
                if (badges.isEmpty()) {
                    badgeViewModel.loadBadges()
                }
            }

            val badge = badges.firstOrNull { it.badgeName == badgeName }
            Log.d("BadgeDetailScreen", "Badge trovato: $badge")

            if (badge != null) {
                BadgeDetailScreen(navController, badge)
            }
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


