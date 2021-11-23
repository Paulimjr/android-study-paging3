package com.androidstudy.service.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidstudy.service.domain.CharacterModel

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterModel>)

    @Query("SELECT * FROM character")
    fun getCharacters(): PagingSource<Int, CharacterModel>

    @Query("DELETE FROM character")
    suspend fun cleanData()
}