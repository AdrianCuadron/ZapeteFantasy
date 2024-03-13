package com.cuadrondev.zapetefantasy.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

fun getLanguageName(code: String) =
    when(code){
        "es" -> "Español"
        "en" -> "English"
        "eu" -> "Euskera"
        else -> {"English"}
    }

fun getLanguageCode(name: String) =
    when(name){
        "Español" -> "es"
        "English" -> "en"
        "Euskera" -> "eu"
        else -> "en"
    }

fun getNameFromLanguageCode(code: String) =
    when(code){
        "es" -> "Español"
        "en" -> "English"
        "eu" -> "Euskera"
        else -> "English"
    }

fun obtenerSimboloMoneda(userCoin: String): String {
    return when (userCoin.lowercase()) {
        "euro" -> "€" // Euro
        "dolar" -> "$" // Dólar
        "libra" -> "£" // Libra
        else -> "€"   // Valor predeterminado si userCoin no coincide con ninguno de los anteriores
    }
}

fun obtenerIniciales(nombre: String, apellido: String): String {
    val inicialNombre = nombre.firstOrNull()?.uppercaseChar() ?: ""
    val inicialApellido = apellido.firstOrNull()?.uppercaseChar() ?: ""

    return "$inicialNombre$inicialApellido"
}
