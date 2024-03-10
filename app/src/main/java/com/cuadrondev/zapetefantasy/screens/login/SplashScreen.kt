package com.cuadrondev.zapetefantasy.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.navigation.AppScreens
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController) {

    LaunchedEffect(true){
        delay(2000)
        navController.popBackStack()
        navController.navigate(AppScreens.LoginScreen.route)
    }

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card {
            Image(painter = painterResource(id = R.mipmap.zapete_fantasy_icon), contentDescription = "")
        }
    }
}