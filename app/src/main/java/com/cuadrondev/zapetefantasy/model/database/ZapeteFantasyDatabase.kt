package com.cuadrondev.zapetefantasy.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cuadrondev.zapetefantasy.model.database.daos.PlayerDao
import com.cuadrondev.zapetefantasy.model.database.daos.UserDao
import com.cuadrondev.zapetefantasy.model.entities.Post
import com.cuadrondev.zapetefantasy.model.entities.Match
import com.cuadrondev.zapetefantasy.model.entities.Player
import com.cuadrondev.zapetefantasy.model.entities.Team
import com.cuadrondev.zapetefantasy.model.entities.User

@Database(entities = [User::class, Player::class, Team::class, Post::class, Match::class], version = 1)
abstract  class ZapeteFantasyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun playerDao(): PlayerDao
}