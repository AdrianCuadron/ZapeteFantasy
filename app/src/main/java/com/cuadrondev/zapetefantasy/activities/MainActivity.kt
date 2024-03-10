package com.cuadrondev.zapetefantasy.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.cuadrondev.zapetefantasy.navigation.MainNavigation
import com.cuadrondev.zapetefantasy.ui.theme.ZapeteFantasyTheme
import com.cuadrondev.zapetefantasy.utils.changeAppLanguage
import com.cuadrondev.zapetefantasy.viewmodels.ZapeteFantasyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: ZapeteFantasyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get extras
        viewModel.initializeLists()


        setContent {
            ZapeteFantasyTheme {
                //initialize language
                Log.d("cambioLang", "main1")
                viewModel.reloadLanguage(viewModel.idioma.collectAsState(initial = viewModel.currentSetLang).value)
                Log.d("cambioLang", "main2")
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    MainNavigation(viewModel)
                }

            }
        }
    }
}