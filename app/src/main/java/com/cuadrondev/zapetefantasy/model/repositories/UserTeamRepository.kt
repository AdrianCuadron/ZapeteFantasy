package com.cuadrondev.zapetefantasy.model.repositories

import android.app.Activity
import com.cuadrondev.zapetefantasy.model.database.daos.PlayerDao
import com.cuadrondev.zapetefantasy.model.database.daos.UserDao
import com.cuadrondev.zapetefantasy.model.datastore.PreferencesDataStore
import com.cuadrondev.zapetefantasy.model.entities.Player
import com.cuadrondev.zapetefantasy.model.entities.Post
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserTeamRepository @Inject constructor(
    private val playerDao: PlayerDao
) {
    suspend fun addPlayer(player: Player) = playerDao.insertPlayer(player)
    fun getMarket() = playerDao.getMarket()
    suspend fun updatePlayer(player: Player) = playerDao.updatePlayer(player)

    suspend fun insertPost(post: Post) = playerDao.insertPost(post)
    fun getPosts() = playerDao.getPosts()
}