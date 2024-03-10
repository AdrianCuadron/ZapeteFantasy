package com.cuadrondev.zapetefantasy.model.repositories

import androidx.datastore.dataStore
import com.cuadrondev.zapetefantasy.model.database.daos.UserDao
import com.cuadrondev.zapetefantasy.model.datastore.PreferencesDataStore
import com.cuadrondev.zapetefantasy.model.entities.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val userDao: UserDao,
    private val datastore: PreferencesDataStore,
) {

    suspend fun addUserData(user: User) = userDao.addUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    fun getUserData(username: String) = userDao.getUserData(username)


    fun getUserTeam(username: String) = userDao.getUserTeam(username)

    fun getStandings() = userDao.getStandings()

    //DATASTORE
    //change language preferences
    suspend fun changeUserLanguage(username: String, lang: String) = datastore.setUserLanguage(username, lang)


}