package com.cuadrondev.zapetefantasy.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.navigation.AppScreens
import com.cuadrondev.zapetefantasy.navigation.BottomBarRoute
import com.cuadrondev.zapetefantasy.navigation.SECTIONS
import com.cuadrondev.zapetefantasy.ui.theme.greenPlayer
import com.cuadrondev.zapetefantasy.utils.obtenerSimboloMoneda
import com.cuadrondev.zapetefantasy.viewmodels.ZapeteFantasyViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen(viewModel: ZapeteFantasyViewModel, navControllerMain: NavHostController) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val windowSizeClass = calculateWindowSizeClass(context as Activity)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: BottomBarRoute.HOME


    val enableBottomNavigation by derivedStateOf { windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact }
    val enableNavigationRail by derivedStateOf { windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact }

    val onNavigateToSection = { route: String ->
        navController.navigate((route)){
            popUpTo(BottomBarRoute.HOME)
            launchSingleTop = true
        }
    }


    Scaffold(
        topBar = { ToolBar(viewModel){navControllerMain.navigate(route = AppScreens.UserScreen.route)} },
        bottomBar = { if (enableBottomNavigation){
            AppBottomBar(selectedDestination, onNavigateToSection)
        } }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (enableNavigationRail){
                ZapeteNavigationRail(selectedDestination, onNavigateToSection)
            }
            NavHost(navController, startDestination = BottomBarRoute.HOME) {
                composable(BottomBarRoute.HOME) {
                    // Contenido de la pestaña Home
                    HomeScreen(viewModel = viewModel)
                }
                composable(BottomBarRoute.MARKET) {
                    // Contenido de la pestaña Market
                    MarketScreen(viewModel = viewModel)
                }
                composable(BottomBarRoute.TEAM) {
                    // Contenido de la pestaña Team
                    TeamScreen(viewModel = viewModel)
                }
                composable(BottomBarRoute.TABLE) {
                    // Contenido de la pestaña Table
                    TableScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ZapeteNavigationRail(currentRoute: String, onNavigate: (String) -> Unit) {
    NavigationRail {
        SECTIONS.forEach { destinations ->

            NavigationRailItem(
                icon = { Icon(ImageVector.vectorResource(id = destinations.selectedIcon), contentDescription = null) },
                selected = currentRoute == destinations.route,
                onClick = { onNavigate(destinations.route) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar(viewModel: ZapeteFantasyViewModel, onUserClick: () -> Unit) {
    val userData by viewModel.userData.collectAsState(initial = User("","","","",0,0.0))
    var username = viewModel.username.value
    var userCoin = viewModel.coin.collectAsState(initial = "").value

    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.Start

            ) {
                //User profile
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { onUserClick() }) {
                    UserAvatar(user = userData)
                    Text(text = username, fontSize = 12.sp, lineHeight = 12.sp)
                }
                Spacer(
                    modifier = Modifier

                        .height(32.dp)

                        .width(2.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                )
                // Columna a la izquierda con "Zapete" en grande y "Fantasy" abajo
                Column(modifier = Modifier
                    .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                    ){
                    Text("ZAPETE", fontSize = 20.sp, lineHeight = 20.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic)
                    Text("FANTASY", fontSize = 14.sp, lineHeight = 14.sp, fontStyle = FontStyle.Italic)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Dinero del usuario en el centro
                Card(modifier = Modifier
                    .padding(4.dp)
                    .border(
                        shape = RoundedCornerShape(16.dp),
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,

                    ),
                    shape = RoundedCornerShape(16.dp),
                    //elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Text("${obtenerSimboloMoneda(userCoin)} ${String.format("%.2f", userData.money).replace(",",".")}M", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp), color = MaterialTheme.colorScheme.primary)
                }

            }
        }
    )
}


@Composable
fun AppBottomBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    NavigationBar {
        SECTIONS.forEach { destinations ->
            NavigationBarItem(
                icon = { Icon(ImageVector.vectorResource(id = destinations.selectedIcon), contentDescription = null) },
                selected = currentRoute == destinations.route,
                onClick = { onNavigate(destinations.route) }
            )
        }
    }
}

