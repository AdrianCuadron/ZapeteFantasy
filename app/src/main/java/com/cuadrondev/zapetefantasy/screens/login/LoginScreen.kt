package com.cuadrondev.zapetefantasy.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cuadrondev.zapetefantasy.R
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.mipmap.zapete_fantasy_icon_no_bg),
                contentDescription = "",
                Modifier.size(82.dp)
            )
            Text(text = "Zapete Fantasy", fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.padding(30.dp)) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                
                Text(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = username, onValueChange = { username = it }, label = {
                    Text(
                        text = "Username"
                    )
                })

                Spacer(modifier = Modifier.height(8.dp))

                var showPassword by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = if (showPassword) R.drawable.visibility_off else R.drawable.visibility),
                                contentDescription = if (showPassword) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(modifier = Modifier.fillMaxWidth(),onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        checkCredentials(viewModel, context, username, password)
                    }
                }) {
                    Text(text = "Login")
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
                    navController.navigate(AppScreens.RegisterScreen.route)
                }) {
                    Text(text = "Sign up")
                }
            }

        }

    }
}

@Composable
fun PasswordTextField(password: String) {
    var password by remember { mutableStateOf("") }

}

suspend fun checkCredentials(
    viewModel: AuthViewModel,
    context: Context,
    username: String,
    password: String
) {
    if (viewModel.checkCredentials(username, password)) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("USERNAME", username)
        context.startActivity(intent)
        (context as Activity).finish()
    }
    else{
        showToastOnMainThread(context, "Usuario y/o contraseña incorrectos")
    }
}