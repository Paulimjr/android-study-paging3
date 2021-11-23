package com.androidstudy.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.androidstudy.service.CharacterService
import com.androidstudy.service.domain.CharacterDB
import com.androidstudy.service.domain.CharacterModel
import com.androidstudy.service.mediator.CharacterRemoteMediator
import com.androidstudy.service.toModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 10

@OptIn(ExperimentalPagingApi::class)
class CharacterRepositoryImpl @Inject constructor(
    private val service: CharacterService,
    private val localCharacters: CharacterDB,
    private val remoteMediator: CharacterRemoteMediator
): CharacterRepository {

    override fun getCharacters(): Flow<PagingData<CharacterModel>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = 2
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = {
            localCharacters.characterDao().getCharacters()
        }
    ).flow

    override suspend fun getCharacterById(id: String) : CharacterModel? {
        val response = service.getCharacterById(id = id)
        return if (response.isSuccessful) {
            response.body()?.toModel()
        } else {
            null
        }
    }
}