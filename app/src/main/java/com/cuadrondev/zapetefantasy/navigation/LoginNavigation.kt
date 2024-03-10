package com.cuadrondev.zapetefantasy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cuadrondev.zapetefantasy.screens.login.LoginScreen
import com.cuadrondev.zapetefantasy.screens.login.SplashScreen

@Composable
fun LoginNavigation() {
    var context = LocalContext.current
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route){
        composable(AppScreens.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(AppScreens.LoginScreen.route){
            LoginScreen(context)
        }
    }
}