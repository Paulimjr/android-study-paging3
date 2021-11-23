package com.androidstudy.service.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.androidstudy.service.CharacterService
import com.androidstudy.service.domain.CharacterDB
import com.androidstudy.service.domain.CharacterModel
import com.androidstudy.service.domain.CharacterRemoteKey
import com.androidstudy.service.toCharacterList
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator @Inject constructor (
    private val service: CharacterService,
    private val db: CharacterDB
): RemoteMediator<Int, CharacterModel>() {

    private val characterDao = db.characterDao()
    private val remoteKeyDao = db.characterRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterModel>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            Log.v("TAG", ">>>>>>>>> loadKey >>> $page")
            val response = service.getCharacters(page)
            val isEndOfList = response.body()?.results?.isEmpty() == true

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.cleanRemoteKeys()
                    characterDao.cleanData()
                }

                val prevKey = if (page == FIRST_PAGE) null else page - FIRST_PAGE
                val nextKey = if (isEndOfList) null else page + FIRST_PAGE

                Log.v("TAG", ">>>>>> page >>>> $page")
                Log.v("TAG", ">>>>>> prevKey >>>> $prevKey")
                Log.v("TAG", ">>>>>> nextKey >>>> $nextKey")
                Log.v("TAG", ">>>>>> isEndOfList >>>> $isEndOfList")

                val keys = response.body()?.results?.map {
                    CharacterRemoteKey(id = it.id, nextKey = nextKey, prevKey = prevKey)
                }

                val characterList = response.body()?.toCharacterList() ?: emptyList()
                remoteKeyDao.insertRemoteKey(keys = keys ?: emptyList())
                characterDao.insertAll(characterList)
            }

            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, CharacterModel>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteClosesToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(FIRST_PAGE) ?: FIRST_PAGE
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                if (remoteKeys?.nextKey == null) return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                prevKey
            }
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, CharacterModel>): CharacterRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { item ->
                remoteKeyDao.remoteKeyByCharacterId(item.id.toString())
            }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getRemoteClosesToCurrentPosition(state: PagingState<Int, CharacterModel>): CharacterRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.remoteKeyByCharacterId(id.toString())
            }
        }
    }

    /**
     * get the gist remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, CharacterModel>): CharacterRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { item -> remoteKeyDao.remoteKeyByCharacterId(item.id.toString()) }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }

}