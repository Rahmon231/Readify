package com.dev.readify.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.readify.navigation.ReadifyScreens

@Composable
fun ReadifyHomeScreen(navController: NavController,
                      homeScreenViewModel: HomeScreenViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
            ReadifyAppBar(title = "Readify",
                showProfile = true,
                navController = navController,
                homeScreenViewModel = homeScreenViewModel){

            }
        },
        floatingActionButton = {
            FABContent{

            }
        },
    ) {
        Surface(
            modifier = Modifier.padding(it).fillMaxSize()
        ) {

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadifyAppBar(title: String,
                  showProfile: Boolean,
                  navController: NavController,
                  homeScreenViewModel: HomeScreenViewModel,
                  content: @Composable () -> Unit) {
    TopAppBar(
        title = {
           Row(verticalAlignment = Alignment.CenterVertically) {
               if (showProfile){
                   Icon(imageVector = Icons.Default.Book,
                       contentDescription = "Profile Icon",
                       modifier = Modifier.clip(RoundedCornerShape(12.dp))
                           .scale(0.9f))
               }
               Text(text = title,
                   color = Color.Red.copy(alpha = 0.7f),
                   style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
               )
               Spacer(modifier = Modifier.width(150.dp))
           }
        },
        actions = {
            IconButton(
                onClick = {
                    homeScreenViewModel.signOut().run {
                        navController.navigate(ReadifyScreens.LoginScreen.name) {
                            popUpTo(ReadifyScreens.HomeScreen.name) { inclusive = true }
                        }
                    }
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = Color(0xFF92CBDF)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent)
    )
}


@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = {onTap()},
        shape = RoundedCornerShape(50.dp),
        containerColor = Color(0xFF92CBDF),
    ) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription = "FAB ADD",
            tint = Color.White)
    }
}
