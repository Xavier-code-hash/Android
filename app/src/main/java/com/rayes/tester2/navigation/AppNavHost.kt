package com.rayes.tester2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rayes.tester2.ui.theme.screens.home.Homescreen
import com.rayes.tester2.ui.theme.screens.intent.IntentScreen
import com.rayes.tester2.ui.theme.screens.login.LogIn
import com.rayes.tester2.ui.theme.screens.register.Register

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route_Home)
{
    NavHost(navController = navController,
        modifier = Modifier,
        startDestination = startDestination){
        composable(Route_Home){
            Homescreen(navController)
        }
        composable(Route_LogIn){
            LogIn(navController)
        }
        composable(Route_Register) {
            Register(navController)
        }
        composable(Route_Intent) {
            IntentScreen(navController)
        }
    }

}