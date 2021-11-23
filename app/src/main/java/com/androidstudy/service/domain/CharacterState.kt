package com.androidstudy.service.domain

sealed class CharacterState {
    object Loading : CharacterState()
    class CharacterFailure(val errorMessage: String? = "") : CharacterState()
    class CharacterSuccess(val result: List<CharacterModel> = emptyList()) : CharacterState()
}
