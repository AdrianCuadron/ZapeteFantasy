package com.cuadrondev.zapetefantasy.screens.login

import android.content.Context
import android.util.Log
import android.widget.Toast
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

                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = {
                    Text(
                        text = "Nombre"
                    )
                })

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = {
                    Text(
                        text = "Apellido"
                    )
                })

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        if (createUser(viewModel, username, password, nombre, apellido, context)) {
                            withContext(Dispatchers.Main) {
                                navController.popBackStack()
                                navController.navigate(AppScreens.LoginScreen.route)
                            }
                        }

                    }
                }) {
                    Text(text = "Crear usuario")
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
    val existe = viewmodel.checkUserExist(username)
    if (existe != null) {
        Toast.makeText(context, "El usuario $username ya existe", Toast.LENGTH_SHORT).show()
        return false
    } else {
        viewmodel.createUser(User(username,password.hash(),nombre,apellido,0,200.0))
        return true
    }

}

