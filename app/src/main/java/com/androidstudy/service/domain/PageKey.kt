package com.androidstudy.service.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_remote_keys")
data class CharacterRemoteKey (

    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "next_key")
    val nextKey: Int?,

    @ColumnInfo(name = "prev_key")
    val prevKey: Int?

)