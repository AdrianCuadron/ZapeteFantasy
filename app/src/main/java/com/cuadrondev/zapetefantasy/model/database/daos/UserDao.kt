package com.cuadrondev.zapetefantasy.model.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.model.entities.UserTeam
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM User where username = :username LIMIT 1")
    fun getUserData(username: String): Flow<User>

    @Query("SELECT * FROM User where username = :username") //añadir que solo sean los jugadores del user
    fun getUserTeam(username: String): Flow<UserTeam>

    @Query("SELECT * FROM User ORDER BY points DESC")
    fun getStandings(): Flow<List<User>>
}