package com.androidstudy.view.usecase

import com.androidstudy.repository.CharacterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterDetailUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(characterId: String) = repository.getCharacterById(characterId)
}
