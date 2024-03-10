package com.cuadrondev.zapetefantasy.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.utils.getLanguageCode
import com.cuadrondev.zapetefantasy.utils.getLanguageName
import com.cuadrondev.zapetefantasy.viewmodels.ZapeteFantasyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@Composable
fun UserScreen(viewModel: ZapeteFantasyViewModel) {
    var username = viewModel.userData.collectAsState(initial = User("", "", "", "", 0, 0.0))
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = username.value.username, fontSize = 32.sp)

            Card {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = stringResource(id = R.string.settings))

                    LanguageDropDown { viewModel.changeLanguage(it) }
                    CoinDropDown { viewModel.changeUserCoin(it) }

                }

            }

            //GUARDAR FICHERO TEXTO
            val contentResolver = LocalContext.current.contentResolver
            val filename = "Noticias.txt"
            val saverLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument()) { uri ->
                if (uri != null) {
                    try {
                        contentResolver.openFileDescriptor(uri, "w")?.use {
                            FileOutputStream(it.fileDescriptor).use { fileOutputStream ->
                                fileOutputStream.write(
                                    (viewModel.obtenerPlantilla()).toByteArray()
                                )
                            }
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            val action = { saverLauncher.launch(filename) }

            Button(onClick = action) {
                Text(text = "Guardar plantilla")
            }
        }
    }
}

@Composable
fun LanguageDropDown(changeLang: (String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var languages = listOf("Español", "English", "Euskera")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier
            .padding(8.dp)
            .clickable { expanded = true }) {
            Icon(Icons.Rounded.Place, contentDescription = null)
            Text(text = stringResource(id = R.string.language))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { lan ->
                DropdownMenuItem(
                    text = { Text(lan) },
                    onClick = { expanded=false
                        changeLang(getLanguageCode(lan)) },
                )
            }
        }
    }
}

@Composable
fun guardarFichero(){
    
}

@Composable
fun CoinDropDown(changeUserCoin: (String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var coins = listOf("Euro", "Dolar", "Libra")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Row(modifier = Modifier
            .padding(8.dp)
            .clickable { expanded = true }) {
            Icon(Icons.Rounded.Place, contentDescription = null)
            Text(text = stringResource(id = R.string.coin))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            coins.forEach { coin ->
                DropdownMenuItem(
                    text = { Text(coin) },
                    onClick = { expanded=false
                        changeUserCoin(coin) },
                )
            }
        }
    }
}

