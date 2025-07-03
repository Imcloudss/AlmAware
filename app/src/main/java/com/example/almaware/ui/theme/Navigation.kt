package com.example.almaware.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.almaware.ui.screens.auth.AuthenticationScreen
import com.example.almaware.ui.screens.auth.sign.SignInScreen
import com.example.almaware.ui.screens.auth.sign.SignUpScreen
import com.example.almaware.ui.screens.splash.SplashScreen
import kotlinx.serialization.Serializable


// ========================================================================================
// NAVIGATION ROUTES - Pattern della prof con sealed interface
// ========================================================================================

sealed interface AlmAwareRoute {
    @Serializable data object Splash : AlmAwareRoute
    @Serializable data object Authentication : AlmAwareRoute
    @Serializable data object SignIn : AlmAwareRoute
    @Serializable data object SignUp : AlmAwareRoute
}

// ========================================================================================
// MAIN NAVIGATION GRAPH
// ========================================================================================

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

        // Authentication Screen
        composable<AlmAwareRoute.Authentication> {
            AuthenticationScreen(navController)
        }

        // Sign Up Screen
        composable<AlmAwareRoute.SignUp> {
            SignUpScreen(navController)
        }

        // Sign In Screen
        composable<AlmAwareRoute.SignIn> {
            SignInScreen(navController)
        }

        // Main App Screens
//        composable<AlmAwareRoute.Home> {
//            HomeScreen(navController)
//        }
//        composable<AlmAwareRoute.Add> {
//            AddTransactionScreen(navController)
//        }
    }
}

// ========================================================================================
// MAIN APP NAVIGATION WRAPPER
// ========================================================================================

    @Composable
    fun AlmAwareNavigation(
        navController: NavHostController = rememberNavController()
    ) {
        AlmAwareNavGraph(navController = navController)
    }

// ========================================================================================
// BOTTOM NAVIGATION HELPER FUNCTIONS
// ========================================================================================

    // Bottom Navigation Item data class
    data class BottomNavItem(
        val route: AlmAwareRoute,
        val title: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector
    )

// Bottom Navigation Items
    val bottomNavItems = listOf(
        BottomNavItem(
            route = AlmAwareRoute.Splash,
            title = "Splash",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            route = AlmAwareRoute.Authentication,
            title = "Authentication",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add
        )
    )

    @Composable
    fun AlmAwareBottomBar(navController: NavController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavigationBar {
            bottomNavItems.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any {
                    it.route == item.route::class.qualifiedName
                } == true

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = if (isSelected) androidx.compose.ui.graphics.Color(0xFF3A6DF0) else LocalContentColor.current
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = if (isSelected) androidx.compose.ui.graphics.Color(0xFF3A6DF0) else LocalContentColor.current
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

// ========================================================================================
// UTILITY FUNCTIONS
// ========================================================================================

    /**
     * Composable centralizzato per mostrare il nome della schermata
     * Usato dalle schermate temporanee
     */
    @Composable
    fun CenteredScreenTitle(
        title: String,
        modifier: Modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
