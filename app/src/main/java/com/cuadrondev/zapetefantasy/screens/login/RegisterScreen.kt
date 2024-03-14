package com.cuadrondev.zapetefantasy.screens.login

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.navigation.AppScreens
import com.cuadrondev.zapetefantasy.utils.hash
import com.cuadrondev.zapetefantasy.viewmodels.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: AuthViewModel,
    context: Context
) {

    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var nombre by rememberSaveable {
        mutableStateOf("")
    }
    var apellido by rememberSaveable {
        mutableStateOf("")
    }

    var showPassword by remember { mutableStateOf(false) }

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
                Text(text = "Sign up", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = username, onValueChange = { username = it }, label = {
                    Text(
                        text = "Username"
                    )
                })

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = if (showPassword) R.drawable.visibility else R.drawable.visibility_off),
                                contentDescription = if (showPassword) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = {
                    Text(
                        text = "Name"
                    )
                }, modifier = Modifier.fillMaxWidth(),)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = apellido, onValueChange = { apellido = it }, label = {
                    Text(
                        text = "Last name"
                    )
                })

                Spacer(modifier = Modifier.height(16.dp))

                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        if (createUser(viewModel, username, password, nombre, apellido, context)) {
                            withContext(Dispatchers.Main) {
                                navController.popBackStack()
                                navController.navigate(AppScreens.LoginScreen.route)
                            }
                        }

                    }
                }) {
                    Text(text = "Create user")
                }
            }

        }

    }
}

fun createUser(
    viewmodel: AuthViewModel,
    username: String,
    password: String,
    nombre: String,
    apellido: String,
    context: Context
): Boolean {
    if (username.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
        showToastOnMainThread(context, "Por favor, complete todos los campos")
        return false
    }
    val existe = viewmodel.checkUserExist(username)
    if (existe != null) {
        Log.d("existe user","entra ${existe}")
        showToastOnMainThread(context, "El usuario $username ya existe")
        return false
    } else {
        Log.d("existe user","no existe")
        viewmodel.createUser(User(username, password.hash(), nombre, apellido, 0, 200.0))
        return true
    }

}

fun showToastOnMainThread(context: Context, message: String) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

