package com.cuadrondev.zapetefantasy.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.entities.Player
import com.cuadrondev.zapetefantasy.model.entities.Post
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.model.entities.UserTeam
import com.cuadrondev.zapetefantasy.ui.theme.dfColor
import com.cuadrondev.zapetefantasy.ui.theme.dlColor
import com.cuadrondev.zapetefantasy.ui.theme.greenField
import com.cuadrondev.zapetefantasy.ui.theme.greenPlayer
import com.cuadrondev.zapetefantasy.ui.theme.mcColor
import com.cuadrondev.zapetefantasy.ui.theme.ptColor
import com.cuadrondev.zapetefantasy.viewmodels.ZapeteFantasyViewModel

@Composable
fun TeamScreen(viewModel: ZapeteFantasyViewModel) {
    var context = LocalContext.current
    //simulamos el equipo con el marketPlayers de momento
    val userTeam by viewModel.userTeamFlow.collectAsState(
        UserTeam(
            User("", "", "", "", 0, 0.0),
            listOf()
        )
    )
    val userData by viewModel.userData.collectAsState(initial = User("","","","",0,0.0))

    //cogemos la alineacion de cada usuario
    val lineUpPlayers: List<Player> = userTeam.players.filter { it.lineUp }
    val orderedPlayerList = userTeam.players.sortedBy { player ->
        when (player.position) {
            "DL" -> 1
            "MC" -> 2
            "DF" -> 3
            "PT" -> 4
            else -> Int.MAX_VALUE // Colocar cualquier posición adicional al final
        }
    }
    var username = viewModel.username.value
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Filas de jugadores

            //ALINEACION
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            shape = RoundedCornerShape(16.dp),
                            width = 3.dp,
                            color = Color.White,
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = greenField),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Ajusta la elevación según tus necesidades
                ) {
                    Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {

                        playerRow(lineUpPlayers.filter { it.position == "DL" }
                            .take(3), 3, viewModel)

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(greenPlayer)
                        ) // Línea blanca en el medio

                        playerRow(lineUpPlayers.filter { it.position == "MC" }
                            .take(3), 3, viewModel)

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Color.White)
                        ) // Otra línea blanca en el medio

                        playerRow(lineUpPlayers.filter { it.position == "DF" }
                            .take(4), 4, viewModel)

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(greenPlayer)
                        ) // Y otra línea más en el medio

                        playerRow(lineUpPlayers.filter { it.position == "PT" }
                            .take(1), 1, viewModel)
                    }
                }

            }

            item {
                Spacer(modifier = Modifier.size(8.dp))
            }

            item {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(id = R.string.team),
                    fontWeight = FontWeight.Bold
                )
            }

            //LISTA DEL EQUIPO
            items(orderedPlayerList) { player ->
                val completed = stringResource(id = R.string.positionCompleted)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .pointerInput(userTeam.players) {
                            detectTapGestures(
                                onPress = {
                                    // Lógica al presionar (opcional)
                                },
                                onLongPress = {

                                    var pos = player.position
                                    if (!player.lineUp) { //añadimos a la alineacion
                                        if (viewModel.obtenerMaxPosicion(pos) > lineUpPlayers.filter { it.position == pos }.size) {
                                            viewModel.updatePlayer(player.copy(lineUp = true))
                                        } else {
                                            Toast
                                                .makeText(
                                                    context,
                                                    completed + pos,
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                }
                            )
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Posicion y Puntos
                        PositionPoints(player)
                        // Escudo a la izquierda
                        TeamIcon(
                            nombreEquipo = player.team,
                            Modifier,
                            viewModel.obtenerEscudo(player.team)
                        )

                        // Espaciador entre la imagen y la descripción
                        Spacer(modifier = Modifier.size(8.dp))

                        // Descripción
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(text = player.name, fontWeight = FontWeight.Bold)
                            Text(text = "${player.price}M") //cambia coin
                        }

                        // Espaciador entre la descripción y el botón
                        Spacer(modifier = Modifier.size(8.dp))

                        // BOTON VENDER
                        Button(onClick = {
                            val newMoney = userData.money + player.price
                            if (newMoney>0.0f){
                                viewModel.updatePlayer(player.copy(user = "", lineUp = false))
                                viewModel.updateUser(userData.copy(money = newMoney))
                                viewModel.insertPost(
                                    Post(
                                        tipo = "compra",
                                        user = username,
                                        texto = "$username ha vendido a ${player.name}"
                                    )
                                )
                            }

                        }) {
                            Text(text = stringResource(id = R.string.sell))
                        }
                    }
                    if (player.lineUp) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .background(MaterialTheme.colorScheme.primary)

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PositionPoints(player: Player) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(colors = CardDefaults.cardColors(containerColor = positionColor(player.position)), modifier = Modifier.padding(4.dp)) {
            Text(text = player.position, fontSize = 12.sp, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold)
        }
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.padding(4.dp)) {
            Text(text = "${player.points}", fontSize = 12.sp, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold)
        }
    }
}

fun positionColor(position: String): Color {
    return when (position) {
        "DL" -> dlColor
        "MC" -> mcColor
        "DF" -> dfColor
        "PT" -> ptColor
        else -> Color.Gray // Puedes cambiar este color por el predeterminado que prefieras
    }
}

@Composable
fun playerRow(players: List<Player>, numPlayer: Int, viewModel: ZapeteFantasyViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        players.forEach { player ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.pointerInput(players) {
                    detectTapGestures(
                        onPress = {
                            // Lógica al presionar (opcional)
                        },
                        onLongPress = {
                            viewModel.updatePlayer(player.copy(lineUp = false))

                        }
                    )
                }) {
                Card(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    colors = CardDefaults.cardColors(greenPlayer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    // Muestra el nombre del jugador y entre paréntesis la posición

                    // Muestra el ícono y el nombre del jugador uno encima del otro
                    TeamIcon(
                        nombreEquipo = player.team,
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        viewModel.obtenerEscudo(player.team)
                    )

                }
                Card(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    colors = CardDefaults.cardColors(greenPlayer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = viewModel.obtenerNombreAlineacion(player.name),
                        modifier = Modifier.padding(4.dp),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }


        val additionalPlayersCount = numPlayer - players.size
        for (i in 0 until additionalPlayersCount) {
            // Agrega una Card adicional por cada jugador adicional
            Card(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(greenPlayer),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                // Muestra el nombre del jugador adicional
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .size(45.dp)
                        .padding(12.dp)
                )
            }
        }
    }
}


@Composable
fun TeamIcon(nombreEquipo: String, modifier: Modifier, icono: Int) {
    val escudoPainter = painterResource(icono)
    Image(
        painter = escudoPainter,
        contentDescription = null, // Agrega una descripción si es necesario
        modifier = modifier
            .padding(4.dp)
            .size(25.dp) // Ajusta el tamaño según tus necesidades
    )
}