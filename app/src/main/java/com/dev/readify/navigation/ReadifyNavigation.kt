package com.dev.readify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dev.readify.splash.ReadifySplashScreen
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
        composable(
            route = ReadifyScreens.DetailsScreen.name + "/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            ReadifyDetailsScreen(navController = navController, bookId = bookId)
        }
        composable(
            route = ReadifyScreens.UpdateScreen.name + "/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            ReadifyUpdateScreen(navController = navController, bookId = bookId)
        }
        composable(ReadifyScreens.StatsScreen.name){
            ReadifyStatsScreen(navController = navController)
        }

    }
}