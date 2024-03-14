package com.cuadrondev.zapetefantasy.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.ui.theme.bronze
import com.cuadrondev.zapetefantasy.ui.theme.gold
import com.cuadrondev.zapetefantasy.ui.theme.gray
import com.cuadrondev.zapetefantasy.ui.theme.greenField
import com.cuadrondev.zapetefantasy.ui.theme.silver
import com.cuadrondev.zapetefantasy.utils.obtenerIniciales
import com.cuadrondev.zapetefantasy.viewmodels.ZapeteFantasyViewModel

@Composable
fun TableScreen(viewModel: ZapeteFantasyViewModel) {
    val standingsFlow by viewModel.standingsFlow.collectAsState(initial = emptyList())

    Surface {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(standingsFlow) { user ->
                val cardColor = when (standingsFlow.indexOf(user)) {
                    0 -> gold // Primer lugar (Oro)
                    1 -> silver // Segundo lugar (Plata)
                    2 -> bronze // Tercer lugar (Bronce)
                    else -> gray // Otros usuarios
                }

                Card(

                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(
                            shape = RoundedCornerShape(16.dp),
                            width = 3.dp,
                            color = cardColor,
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(color = Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        //posicion
                        Text(
                            text = "${standingsFlow.indexOf(user) + 1}.",
                            fontWeight = FontWeight.Bold
                        )
                        // Foto a la izquierda (iniciales nombre apellido)
                        UserAvatar(user = user)

                        // Espaciador entre la imagen y la descripción
                        Spacer(modifier = Modifier.size(8.dp))

                        // Descripción
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = user.username, fontWeight = FontWeight.Bold)
                        }

                        // Espaciador entre la descripción y el botón
                        Spacer(modifier = Modifier.size(32.dp))


                        Text(text = user.points.toString(), fontWeight = FontWeight.Bold)
                        Text(text = "PTS.", fontSize = 8.sp, modifier = Modifier.alpha(0.5f))

                    }
                }
            }
        }
    }
}

@Composable
fun UserAvatar(user: User) {
    val initials = obtenerIniciales(user.name, user.lastname)

    Box(
        modifier = Modifier
            .size(30.dp) // Ajusta el tamaño del círculo según tus necesidades
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Center),
            fontWeight = FontWeight.Bold
        )
    }
}

