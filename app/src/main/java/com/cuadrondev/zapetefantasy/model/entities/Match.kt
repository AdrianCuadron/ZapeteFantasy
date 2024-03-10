package com.cuadrondev.zapetefantasy.model.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "Match")
data class Match(
    @PrimaryKey
    val id: Int = 0,
    val local: String,
    val visitante: String,
    val dia: String,
    val hora: String,
)
