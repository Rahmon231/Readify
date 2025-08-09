package com.dev.readify.navigation

enum class ReadifyScreens {
    SplashScreen,
    LoginScreen,
    SignupScreen,
    HomeScreen,
    SearchScreen,
    DetailsScreen,
    UpdateScreen,
    StatsScreen;
    companion object {
        fun fromRoute(route: String?): ReadifyScreens
        = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            SignupScreen.name -> SignupScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            DetailsScreen.name -> DetailsScreen
            UpdateScreen.name -> UpdateScreen
            StatsScreen.name -> StatsScreen
            null -> HomeScreen
            else -> throw java.lang.IllegalArgumentException("Route $route is not recognized")
        }

    }
}