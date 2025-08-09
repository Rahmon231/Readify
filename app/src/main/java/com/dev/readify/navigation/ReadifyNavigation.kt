package com.dev.readify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.readify.screens.ReadifySplashScreen
import com.dev.readify.screens.details.ReadifyDetailsScreen
import com.dev.readify.screens.home.ReadifyHomeScreen
import com.dev.readify.screens.login.ReadifyLoginScreen
import com.dev.readify.screens.search.ReadifySearchScreen
import com.dev.readify.screens.signup.ReadifySignupScreen
import com.dev.readify.screens.stats.ReadifyStatsScreen
import com.dev.readify.screens.update.ReadifyUpdateScreen

@Composable
fun ReadifyNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReadifyScreens.SplashScreen.name) {
        composable(ReadifyScreens.SplashScreen.name) {
            ReadifySplashScreen(navController = navController)
        }
        composable(ReadifyScreens.SignupScreen.name) {
            ReadifySignupScreen(navController = navController)
        }
        composable(ReadifyScreens.LoginScreen.name) {
            ReadifyLoginScreen(navController = navController)
        }
        composable(ReadifyScreens.HomeScreen.name) {
            ReadifyHomeScreen(navController = navController)
        }
        composable(ReadifyScreens.SearchScreen.name){
            ReadifySearchScreen(navController = navController)
        }
        composable(ReadifyScreens.DetailsScreen.name){
            ReadifyDetailsScreen(navController = navController)
        }
        composable(ReadifyScreens.UpdateScreen.name){
            ReadifyUpdateScreen(navController = navController)
        }
        composable(ReadifyScreens.StatsScreen.name){
            ReadifyStatsScreen(navController = navController)
        }

    }
}