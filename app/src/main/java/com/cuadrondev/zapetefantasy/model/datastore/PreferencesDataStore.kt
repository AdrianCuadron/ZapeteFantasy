package com.cuadrondev.zapetefantasy.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

val Context.datastore: DataStore<Preferences> by preferencesDataStore("ZapeteFantasy")

private object PreferencesKeys{
    fun USER_LANG(user: String) = stringPreferencesKey("${user}_lang")
    fun USER_COIN(user: String) = stringPreferencesKey("${user}_coin")
}


@Singleton
class PreferencesDataStore @Inject constructor(
     @ApplicationContext context: Context
) {

    private val myPreferencesDataStore = context.datastore

    suspend fun setUserLanguage(username: String, langCode: String) {
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_LANG(username)] = langCode
        }
    }

    suspend fun setUserCoin(username: String, coin: String) {
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_COIN(username)] = coin
        }
    }

    fun getUserLanguage(username: String): Flow<String> {
        return myPreferencesDataStore.data.map { it[PreferencesKeys.USER_LANG(username)] ?: Locale.getDefault().language }
    }

    fun getUserCoin(username: String): Flow<String> {
        return myPreferencesDataStore.data.map { it[PreferencesKeys.USER_COIN(username)] ?: "" }

    }
}