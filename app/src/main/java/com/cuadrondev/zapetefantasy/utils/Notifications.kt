package com.cuadrondev.zapetefantasy.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.activities.LoginActivity

fun crearNotificacion(context: Context, playerName: String) {
    //constantes
    val CHANNEL_ID = "Prueba"
    val CHANNEL_NAME = "Prueba"
    val NOTIFICATION_ID = 1

    createNotificationChannel(context, CHANNEL_ID, CHANNEL_NAME)

    // Intent para abrir la aplicación
    val intent = Intent(context, LoginActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val requestCode = playerName.hashCode()

    val intentShare = Intent(Intent.ACTION_SEND)
    intentShare.type = "text/plain"

    val intentShareCopy = Intent(intentShare)  // Crear una copia del intent original
    intentShareCopy.putExtra(Intent.EXTRA_TEXT, "Acabo de comprar a $playerName!\nUnete a mi liga en ZAPETE FANTASY!")
    Log.d("compartir", playerName)

    var builder = NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.mipmap.zapete_fantasy_icon)
        .setContentTitle("Has comprado a ${playerName}").setContentText("Buenas suerte!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .addAction(android.R.drawable.ic_menu_share,"Compartir", PendingIntent.getActivity(context, requestCode, intentShareCopy,
            PendingIntent.FLAG_IMMUTABLE))
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@with
        }
        // notificationId is a unique int for each notification that you must define.
        notify(NOTIFICATION_ID, builder.build())
    }

}

private fun createNotificationChannel(
    context: Context, CHANNEL_ID: String, CHANNEL_NAME: String
) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val descriptionText = "Prueba"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}