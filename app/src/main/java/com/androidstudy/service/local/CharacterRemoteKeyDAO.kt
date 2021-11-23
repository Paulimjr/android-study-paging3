package com.androidstudy.service.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidstudy.service.domain.CharacterRemoteKey

@Dao
interface CharacterRemoteKeyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(keys: List<CharacterRemoteKey>)

    @Query("SELECT * FROM character_remote_keys WHERE id = :id")
    suspend fun remoteKeyByCharacterId(id: String): CharacterRemoteKey?

    @Query("DELETE FROM character_remote_keys")
    suspend fun cleanRemoteKeys()
}