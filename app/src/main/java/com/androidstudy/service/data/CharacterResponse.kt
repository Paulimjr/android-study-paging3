package com.androidstudy.service.data

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("info") val pageInfo: PageInfo,
    val results: List<Character> = listOf()
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String?
)
data class Character(
    val id: Int,
    val name: String,
    val species: String,
    val type: String,
    val image: String,
    val url: String,
    val episode: List<String>
)
