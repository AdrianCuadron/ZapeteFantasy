package com.cuadrondev.zapetefantasy.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.entities.Post
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.ui.theme.duda
import com.cuadrondev.zapetefantasy.ui.theme.lesionado
import com.cuadrondev.zapetefantasy.ui.theme.roja
import com.cuadrondev.zapetefantasy.utils.crearNotificacion
import com.cuadrondev.zapetefantasy.viewmodels.ZapeteFantasyViewModel


@Composable
fun MarketScreen(viewModel: ZapeteFantasyViewModel) {
    var context = LocalContext.current
    val marketPlayers by viewModel.marketFlow.collectAsState(emptyList())
    val userData by viewModel.userData.collectAsState(initial = User("","","","",0,0.0))
    var username = viewModel.username.value
    
    var notMoney = stringResource(id = R.string.notMoney)
    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        LazyColumn {
            items(marketPlayers) { player ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PositionPoints(player = player)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Foto a la izquierda
                            TeamIcon(
                                nombreEquipo = player.team,
                                modifier = Modifier,
                                viewModel.obtenerEscudo(player.team)
                            )
                            EstadoJugador(player.state)
                        }


                        // Espaciador entre la imagen y la descripción
                        Spacer(modifier = Modifier.size(8.dp))

                        // Descripción
                        Column(
                            modifier = Modifier
                                .weight(1f) //OCUPA el espacio total y el boton se pone a la derecha
                        ) {
                            Text(text = player.name, fontWeight = FontWeight.Bold)
                            Text(text = player.team)
                        }

                        // Espaciador entre la descripción y el botón
                        Spacer(modifier = Modifier.size(8.dp))

                        // BOTON COMPRAR JUGADOR
                        Button(onClick = {
                            val newMoney = userData.money - player.price
                            val newPoints = userData.points + player.points
                            if (newMoney>0.0){
                                viewModel.updatePlayer(player.copy(user = username))
                                viewModel.updateUser(userData.copy(money = newMoney, points = newPoints))
                                viewModel.insertPost(Post(tipo = "compra", user = username, texto = "$username ha comprado a ${player.name}"))
                                crearNotificacion(context,player.name)
                            }
                            else{
                                Toast
                                    .makeText(
                                        context,
                                        notMoney,
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }

                        }) {
                            Text(text = "${player.price}M") //cambiar coin
                        }

                        IconButton(onClick = { infoJugador(context) }) {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EstadoJugador(state: String) {
    var texto = ""
    when (state) {
        "Lesionado" -> {
            Card(colors = CardDefaults.cardColors(containerColor = lesionado), modifier = Modifier.padding(4.dp), shape = CircleShape) {
                Text(text = "  +  ", fontSize = 12.sp, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        "Duda" -> {
            Card(colors = CardDefaults.cardColors(containerColor = duda), modifier = Modifier.padding(4.dp), shape = CircleShape) {
                Text(text = "  ?  ", fontSize = 12.sp, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        "Roja" -> {
            Card(colors = CardDefaults.cardColors(containerColor = roja), modifier = Modifier.padding(4.dp), shape = CircleShape) {
                Text(text = "  R  ", fontSize = 12.sp, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        else -> {
            // Manejar otros tipos de elementos si es necesario
        }
    }
}

fun infoJugador(context: Context) {
    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sofascore.com/tournament/football/spain/laliga/8"))
    context.startActivity(i)
}
