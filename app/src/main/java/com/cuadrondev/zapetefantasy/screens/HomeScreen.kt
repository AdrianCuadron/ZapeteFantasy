package com.cuadrondev.zapetefantasy.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.entities.Post
import com.cuadrondev.zapetefantasy.viewmodels.ZapeteFantasyViewModel
import kotlin.system.exitProcess

@Composable
fun HomeScreen(viewModel: ZapeteFantasyViewModel) {

    var showExitAlertDialog by rememberSaveable { mutableStateOf(false) }

    if (showExitAlertDialog){

        AlertDialog(
            title = { Text(stringResource(id = R.string.exit), textAlign = TextAlign.Center) },
            text = { Text("") },
            confirmButton = {
                TextButton(onClick = { exitProcess(0) }) {
                    Text(text = stringResource(id = R.string.exit_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitAlertDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel_button))
                }
            },
            onDismissRequest = { showExitAlertDialog = false }
        )

    }

    BackHandler {  showExitAlertDialog = true }


    if (viewModel.dialogoPost.value) {
        DialogoPost(onConfirmation = { texto ->
            viewModel.insertPost(Post(tipo = "publicacion",user = viewModel.username.value, texto = texto))
            viewModel.dialogoPost.value = false

        }) { viewModel.dialogoPost.value = false }
    }
    Box(
        Modifier
            .fillMaxSize()
            //.background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {

                Text(text = stringResource(id = R.string.matchday), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                ListaPartidos(viewModel)
                Spacer(modifier = Modifier.height(16.dp))

            }

            //Actividad
            item {
                Text(text = stringResource(id = R.string.news), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Actividades(viewModel)
            }
        }

        FloatingActionButton(
            onClick = { viewModel.dialogoPost.value = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            containerColor = MaterialTheme.colorScheme.primary

        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.edit_new),
                contentDescription = null
            )
        }

    }
}

@Composable
fun DialogoPost(onConfirmation: (String) -> Unit, onDismissRequest: () -> Unit) {
    var texto by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {//tiene que aceptar el dialogo para que se cierre
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id = R.string.newPost), fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    value = texto,
                    shape = RoundedCornerShape(25.dp),
                    onValueChange = {
                        texto = it
                    }
                )

                Button(
                    onClick = { if (texto!="") onConfirmation(texto) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Text(stringResource(id = R.string.postButton))
                }

            }
        }
    }
}

@Composable
fun Actividades(viewModel: ZapeteFantasyViewModel) {
    val activities by viewModel.postsFlow.collectAsState(emptyList())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        activities.forEach { item ->
            when (item.tipo) {
                "compra", "venta" -> {
                    TransferenciaCard(item)
                }

                "publicacion" -> {
                    PublicacionCard(item)
                }

                else -> {
                    // Manejar otros tipos de elementos si es necesario
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TransferenciaCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        //colors = CardDefaults.cardColors(containerColor = transferenciaColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.ShoppingCart,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${post.texto}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(4.dp),
                //color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Agrega otros elementos según tus necesidades
        }
    }
}

@Composable
fun PublicacionCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        //colors = CardDefaults.cardColors(containerColor = publicacionColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Send,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = "${post.user}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${post.texto}",
                    fontSize = 14.sp,
                    //color = Color.White
                )
            }
        }

    }
}


@Composable
fun ListaPartidos(viewModel: ZapeteFantasyViewModel) {
    LazyRow(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(viewModel.listaPartidos.value) { partido ->
            Card(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(200.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // A la izquierda: Equipo local
                    TeamIcon(
                        nombreEquipo = partido.local,
                        Modifier, viewModel.obtenerEscudo(partido.local)
                    )

                    // En el medio: Hora y fecha
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = partido.dia,
                            fontSize = 12.sp,
                            lineHeight = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = partido.hora,
                            fontSize = 12.sp,
                            lineHeight = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // A la derecha: Equipo visitante
                    TeamIcon(
                        nombreEquipo = partido.visitante,
                        Modifier,
                        viewModel.obtenerEscudo(partido.visitante)
                    )
                }
            }
        }
    }
}
