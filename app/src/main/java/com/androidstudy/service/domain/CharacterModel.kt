package com.androidstudy.service.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character")
data class CharacterModel(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String
)
