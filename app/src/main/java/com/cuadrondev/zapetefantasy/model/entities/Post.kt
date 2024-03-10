package com.cuadrondev.zapetefantasy.model.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "Post")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipo: String,
    val user: String,
    val texto: String,
)
