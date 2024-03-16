package com.cuadrondev.zapetefantasy.model.database.daos

import android.app.Activity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cuadrondev.zapetefantasy.model.entities.Player
import com.cuadrondev.zapetefantasy.model.entities.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert
    suspend fun insertPlayer(player: Player)

    @Insert
    suspend fun insertPost(post: Post)

    @Update
    suspend fun updatePlayer(player: Player)

    @Query("DELETE FROM Player WHERE name = :player")
    fun deletePlayer(player: String)

    @Query("SELECT * FROM Player WHERE user==''")
    fun getMarket(): Flow<List<Player>>

    @Query("SELECT * FROM Post ORDER BY id DESC")
    fun getPosts(): Flow<List<Post>>
}