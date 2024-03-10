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

fun getCoinSymbol(code: String) =
    when(code){
        "Euro" -> "€"
        "Dolar" -> "$"
        else -> {"$"}
    }

fun changeAppLanguage(s: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(s))
}

fun obtenerIniciales(nombre: String, apellido: String): String {
    val inicialNombre = nombre.firstOrNull()?.uppercaseChar() ?: ""
    val inicialApellido = apellido.firstOrNull()?.uppercaseChar() ?: ""

    return "$inicialNombre$inicialApellido"
}
