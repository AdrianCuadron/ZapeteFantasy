package com.cuadrondev.zapetefantasy.model.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "User")
data class User(
    @PrimaryKey
    val username: String,
    val password: String = "",
    val name: String,
    val lastname: String,
    val points: Int,
    val money: Double
)
