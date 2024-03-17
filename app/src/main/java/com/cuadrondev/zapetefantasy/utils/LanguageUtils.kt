package com.cuadrondev.zapetefantasy.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


//App's Language Manager

@Singleton
class LanguageManager @Inject constructor() {

    // Current application's lang
    var currentLang: String = Locale.getDefault().language.lowercase()

    // Method to change the App's language setting a new locale
    fun changeLang(lang: String) {

        val localeList = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(localeList)
        currentLang = lang

    }


}