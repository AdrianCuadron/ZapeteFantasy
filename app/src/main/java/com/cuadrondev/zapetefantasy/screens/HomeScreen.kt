package com.cuadrondev.zapetefantasy.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.entities.Post
import com.cuadrondev.zapetefantasy.ui.theme.publicacionColor
import com.cuadrondev.zapetefantasy.ui.theme.transferenciaColor
import com.cuadrondev.zapetefantasy.viewmodels.ZapeteFantasyViewModel

@Composable
fun HomeScreen(viewModel: ZapeteFantasyViewModel) {
    if (viewModel.dialogoPost.value) {
        DialogoPost(onConfirmation = { texto ->
            viewModel.insertPost(Post(tipo = "publicacion",user = viewModel.username.value, texto = texto))
            viewModel.dialogoPost.value = false

        }) { viewModel.dialogoPost.value = false }
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Titulo
            item {
                Text(text = "LaLiga")
            }

            item {
                Column {
                    Text(text = stringResource(id = R.string.matchday))
                }
                ListaPartidos(viewModel)

            }

            //Actividad
            item {
                Text(text = stringResource(id = R.string.news))
                Actividades(viewModel)
            }
        }

        FloatingActionButton(
            onClick = { viewModel.dialogoPost.value = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
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
        colors = CardDefaults.cardColors(containerColor = transferenciaColor),
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
            Text(
                text = "${post.texto}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(4.dp)
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
        colors = CardDefaults.cardColors(containerColor = publicacionColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Send,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "${post.user}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${post.texto}",
                    fontSize = 14.sp
                )
            }
        }

    }
}


@Composable
fun ListaPartidos(viewModel: ZapeteFantasyViewModel) {
    LazyRow(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(viewModel.listaPartidos.value) { partido ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(200.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
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
                            .fillMaxHeight()
                            .padding(horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = partido.dia,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = partido.hora,
                            fontSize = 12.sp,
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
