package com.dev.readify.screens.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dev.readify.components.ReadifyAppBar
import com.dev.readify.navigation.ReadifyScreens

@Composable
fun ReadifySearchScreen(navController: NavController){
    Scaffold(
        topBar = {
            ReadifyAppBar(
                title = "Search Books",
                showProfile = false,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                navController = navController){
                navController.navigate(ReadifyScreens.HomeScreen.name)
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {  }
    }

}