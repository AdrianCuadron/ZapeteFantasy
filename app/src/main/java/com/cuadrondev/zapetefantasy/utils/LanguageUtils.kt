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


/*************************************************
 **          App's Available Languages          **
 *************************************************/

/**
 * Class containing the App's available languages.
 *
 * @property language Full name of that language (in that language)
 * @property code Language's locale code
 */
enum class AppLanguage(val language: String, val code: String) {
    EN("English", "en"),
    EU("Euskera", "eu"),
    ES("Español", "es");


    companion object {
        /**
         * Get the [AppLanguage] from a language code.
         *
         * @param code Language's code as string
         * @return That code's corresponding App's language as an [AppLanguage]. Defaults to [EN].
         */
        fun getFromCode(code: String) = when (code) {
            EU.code -> EU
            EN.code -> EN
            ES.code -> ES
            else -> EN
        }
    }
}


/*************************************************
 **            App's Language Manager           **
 *************************************************/

/**
 * Class to manage the current app's language.
 *
 * It is annotated with Hilt's singleton annotation so only one instance is created in the whole Application.
 */
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