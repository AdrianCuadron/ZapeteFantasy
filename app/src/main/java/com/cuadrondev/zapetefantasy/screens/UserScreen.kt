package com.cuadrondev.zapetefantasy.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.utils.getLanguageCode
import com.cuadrondev.zapetefantasy.utils.getLanguageName
import com.cuadrondev.zapetefantasy.utils.obtenerSimboloMoneda
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
    var userData = viewModel.userData.collectAsState(initial = User("", "", "", "", 0, 0.0))
    var userLang = viewModel.idioma.collectAsState(initial = viewModel.currentSetLang).value
    var userCoin = viewModel.coin.collectAsState(initial = "").value
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {


                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    UserAvatar(user = userData.value)
                    Text(text = userData.value.username, fontSize = 12.sp, lineHeight = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${userData.value.name} ${userData.value.lastname}", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Card {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.settings),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LanguageDropDown(userLang) { viewModel.changeLanguage(it) }
                        CoinDropDown(userCoin) { viewModel.changeUserCoin(it) }
                    }

                }

                Spacer(modifier = Modifier.height(32.dp))

                //GUARDAR FICHERO TEXTO
                val contentResolver = LocalContext.current.contentResolver
                val filename = "Noticias.txt"
                val saverLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument()) { uri ->
                        if (uri != null) {
                            try {
                                contentResolver.openFileDescriptor(uri, "w")?.use {
                                    FileOutputStream(it.fileDescriptor).use { fileOutputStream ->
                                        fileOutputStream.write(
                                            (viewModel.obtenerPosts()).toByteArray()
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

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = action) {
                    Text(text = stringResource(id = R.string.buy_newspaper))
                }
            }
        }
    }
}

@Composable
fun LanguageDropDown(userLang: String, changeLang: (String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var languages = listOf("Español", "English", "Euskera")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(ImageVector.vectorResource(id = R.drawable.language), contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.language) + ": ${getLanguageName(userLang)}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { lan ->
                DropdownMenuItem(
                    text = { Text(lan) },
                    onClick = {
                        expanded = false
                        changeLang(getLanguageCode(lan))
                    },
                )
            }
        }
    }
}

@Composable
fun CoinDropDown(userCoin: String, changeUserCoin: (String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var coins = listOf("Euro", "Dolar", "Libra")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(8.dp)
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(ImageVector.vectorResource(id = R.drawable.coin), contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.coin) + ": ${obtenerSimboloMoneda(userCoin.lowercase())}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            coins.forEach { coin ->
                DropdownMenuItem(
                    text = { Text(coin) },
                    onClick = {
                        expanded = false
                        changeUserCoin(coin)
                    },
                )
            }
        }
    }
}

