package com.androidstudy.service.domain

sealed class CharacterDetailState {
    object Loading : CharacterDetailState()
    class CharacterFailure(val errorMessage: String?) : CharacterDetailState()
    class CharacterSuccess(val result: CharacterModel?) : CharacterDetailState()
}
