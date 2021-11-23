package com.androidstudy.repository

import androidx.paging.PagingData
import com.androidstudy.service.domain.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacters(): Flow<PagingData<CharacterModel>>

    suspend fun getCharacterById(id: String) : CharacterModel?
}
