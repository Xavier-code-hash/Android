package com.rayes.tester2.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rayes.tester2.ui.theme.screens.about.AboutScreen
import com.rayes.tester2.ui.theme.screens.calculator.Calculator
import com.rayes.tester2.ui.theme.screens.candycrush.CandyCrushScreen
import com.rayes.tester2.ui.theme.screens.cards.CardScreen
import com.rayes.tester2.ui.theme.screens.contact.ContactScreen
import com.rayes.tester2.ui.theme.screens.dashboard.DashboardScreen
import com.rayes.tester2.ui.theme.screens.form.FormScreen
import com.rayes.tester2.ui.theme.screens.home.Homescreen
import com.rayes.tester2.ui.theme.screens.intent.CalculatedIntentScreen
import com.rayes.tester2.ui.theme.screens.login.LogIn
import com.rayes.tester2.ui.theme.screens.mickeyadventures.MickeyAdventuresScreen
import com.rayes.tester2.ui.theme.screens.onboarding.OnboardingScreen
import com.rayes.tester2.ui.theme.screens.register.Register
import com.rayes.tester2.ui.theme.screens.spaceshooter.SpaceShooterScreen
import com.rayes.tester2.ui.theme.screens.splash.SplashScreen
import com.rayes.tester2.ui.theme.screens.sudoku.SudokuScreen

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(Route_Home, Icons.Default.Home, "Home")
    object Dashboard : BottomNavItem(Route_Dashboard, Icons.Default.Dashboard, "Games")
    object Profile : BottomNavItem(Route_LogIn, Icons.Default.Person, "Account")
    object Settings : BottomNavItem(Route_Form, Icons.Default.Settings, "Settings")
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route_Splash
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Hide bottom bar on Splash, Onboarding, and Games
    val fullScreenRoutes = listOf(
        Route_Splash, 
        Route_Onboarding, 
        Route_CandyCrush, 
        Route_Sudoku, 
        Route_SpaceShooter,
        Route_MickeyAdventures
    )
    val showBottomBar = currentDestination?.route !in fullScreenRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val items = listOf(
                        BottomNavItem.Home,
                        BottomNavItem.Dashboard,
                        BottomNavItem.Profile,
                        BottomNavItem.Settings
                    )
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(if (showBottomBar) innerPadding else PaddingValues()),
            startDestination = startDestination
        ) {
            composable(Route_Splash) {
                SplashScreen(navController)
            }
            composable(Route_Onboarding) {
                OnboardingScreen(navController)
            }
            composable(Route_Home) {
                Homescreen(navController)
            }
            composable(Route_LogIn) {
                LogIn(navController)
            }
            composable(Route_Register) {
                Register(navController)
            }
            composable(Route_Intent) {
                CalculatedIntentScreen(navController)
            }
            composable(Route_Calculator) {
                Calculator(navController)
            }
            composable(Route_Card) {
                CardScreen()
            }
            composable(Route_Dashboard) {
                DashboardScreen(navController)
            }
            composable(Route_About) {
                AboutScreen(navController)
            }
            composable(Route_Contact) {
                ContactScreen(navController)
            }
            composable(Route_Form) {
                FormScreen(navController)
            }
            composable(Route_CandyCrush) {
                CandyCrushScreen(navController)
            }
            composable(Route_Sudoku) {
                SudokuScreen(navController)
            }
            composable(Route_SpaceShooter) {
                SpaceShooterScreen(navController)
            }
            composable(Route_MickeyAdventures) {
                MickeyAdventuresScreen(navController)
            }
        }
    }
}
