package com.cuadrondev.zapetefantasy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cuadrondev.zapetefantasy.screens.MainScreen
import com.cuadrondev.zapetefantasy.screens.UserScreen
import com.cuadrondev.zapetefantasy.screens.login.LoginScreen
import com.cuadrondev.zapetefantasy.viewmodels.ZapeteFantasyViewModel

@Composable
fun MainNavigation(viewModel: ZapeteFantasyViewModel) {
    var context = LocalContext.current
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.MainScreen.route){
        composable(AppScreens.MainScreen.route){
            MainScreen(viewModel, navController)
        }
        composable(AppScreens.UserScreen.route){
            UserScreen(viewModel)
        }
    }
}