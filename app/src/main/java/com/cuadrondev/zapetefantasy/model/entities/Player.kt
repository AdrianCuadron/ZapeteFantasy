package com.cuadrondev.zapetefantasy.model.entities

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Immutable
@Entity(tableName = "Player")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val user: String = "", //id relacionado
    val team: String,
    val position: String,
    val lineUp: Boolean,
    val points: Int,
    val price: Double,
    val state: String = ""
)

data class UserTeam(
    @Embedded val user: User,
    @Relation(
        parentColumn = "username",
        entityColumn = "user"
    )
    val players: List<Player>
)