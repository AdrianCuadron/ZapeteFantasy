package com.cuadrondev.zapetefantasy.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.cuadrondev.zapetefantasy.R
import kotlin.coroutines.coroutineContext

data class BottomBarActions(
    var route: String,
    var selectedIcon: Int
)

val SECTIONS = listOf(
    BottomBarActions(
        route = BottomBarRoute.HOME,
        selectedIcon = R.drawable.home_icon,

    ),
    BottomBarActions(
        route = BottomBarRoute.MARKET,
        selectedIcon = R.drawable.market_icon,

    ),
    BottomBarActions(
        route = BottomBarRoute.TEAM,
        selectedIcon = R.drawable.team_icon,

    ),
    BottomBarActions(
        route = BottomBarRoute.TABLE,
        selectedIcon = R.drawable.trophy_icon,

    )
)

object BottomBarRoute{
    const val HOME = "home"
    const val MARKET = "market"
    const val TEAM = "team"
    const val TABLE = "table"
}
