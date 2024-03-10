package com.cuadrondev.zapetefantasy.model.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "Team")
data class Team(
    @PrimaryKey
    val nombre: String
)
