package com.cuadrondev.zapetefantasy.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cuadrondev.zapetefantasy.activities.MainActivity
import com.cuadrondev.zapetefantasy.navigation.AppScreens
import com.cuadrondev.zapetefantasy.viewmodels.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, viewModel: AuthViewModel, context: Context) {
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card() {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(value = username, onValueChange = { username = it }, label = {
                    Text(
                        text = "Username"
                    )
                })

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = password, onValueChange = { password = it }, label = {
                    Text(
                        text = "Password"
                    )
                })

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { coroutineScope.launch(Dispatchers.IO) {
                    checkCredentials(viewModel, context, username, password)
                } }) {
                    Text(text = "Acceder")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    navController.navigate(AppScreens.RegisterScreen.route)
                }) {
                    Text(text = "Registrarse")
                }
            }

        }

    }
}

suspend fun checkCredentials(viewModel: AuthViewModel, context: Context, username: String, password: String) {
    if (viewModel.checkCredentials(username,password)){
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("USERNAME", username)
        context.startActivity(intent)
        (context as Activity).finish()
    }


}