package com.androidstudy.service

import com.androidstudy.service.data.Character
import com.androidstudy.service.data.CharacterResponse
import com.androidstudy.service.domain.CharacterModel

fun Character.toModel() = CharacterModel(
    id = id,
    name = name,
    image = image,
)

fun CharacterResponse.toCharacterList(): List<CharacterModel> {
    val items = mutableListOf<CharacterModel>()
    this.results.map {
        items.add(it.toModel())
    }
    return items
}
