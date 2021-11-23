package com.androidstudy.service

import com.androidstudy.service.data.Character
import com.androidstudy.service.data.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): Response<CharacterResponse>

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: String
    ): Response<Character>
}
