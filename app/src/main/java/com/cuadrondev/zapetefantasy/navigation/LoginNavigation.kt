package com.cuadrondev.zapetefantasy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cuadrondev.zapetefantasy.screens.login.LoginScreen
import com.cuadrondev.zapetefantasy.screens.login.RegisterScreen
import com.cuadrondev.zapetefantasy.screens.login.SplashScreen
import com.cuadrondev.zapetefantasy.viewmodels.AuthViewModel

@Composable
fun LoginNavigation(viewModel: AuthViewModel) {
    var context = LocalContext.current
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route){
        composable(AppScreens.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(AppScreens.LoginScreen.route){
            LoginScreen(navController, viewModel, context)
        }
        composable(AppScreens.RegisterScreen.route){
            RegisterScreen(navController,viewModel,context)
        }
    }
}