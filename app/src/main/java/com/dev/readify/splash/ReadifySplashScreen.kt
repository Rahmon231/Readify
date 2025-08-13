package com.dev.readify.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.readify.components.ReadifyLogo
import com.dev.readify.navigation.ReadifyScreens
import kotlinx.coroutines.delay

@Composable
fun ReadifySplashScreen(navController : NavController,
                        splashViewModel: SplashViewModel = hiltViewModel()
){
    val scale = remember {
        Animatable(0f)
    }
    var navigated by remember { mutableStateOf(false) }

    val isAuthenticated by splashViewModel.isAuthenticated.collectAsState()
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }))
    }
    LaunchedEffect(isAuthenticated) { // react whenever isAuthenticated changes
        if (!navigated && isAuthenticated != null) {
            navigated = true
            delay(2000L) // splash delay
            if (isAuthenticated == true) {
                navController.navigate(ReadifyScreens.HomeScreen.name) {
                    popUpTo(ReadifyScreens.SplashScreen.name) { inclusive = true }
                }
            } else {
                navController.navigate(ReadifyScreens.LoginScreen.name) {
                    popUpTo(ReadifyScreens.SplashScreen.name) { inclusive = true }
                }
            }
        }
    }


    Surface(modifier = Modifier
        .padding(15.dp)
        .size(330.dp)
        .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            ReadifyLogo()
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Read, Learn, Repeat",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.LightGray)

        }
    }


}

