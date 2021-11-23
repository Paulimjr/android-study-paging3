package com.androidstudy.viewmodel

import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstudy.service.domain.CharacterDetailState
import com.androidstudy.view.usecase.CharacterDetailUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val SS_CHARACTER_ID = "ss.characterId"

class DetailViewModel @AssistedInject constructor(
    private val characterDetailUseCase: CharacterDetailUseCase
): ViewModel() {

    private val _viewState: MutableStateFlow<CharacterDetailState> = MutableStateFlow(CharacterDetailState.Loading)
    val viewState: StateFlow<CharacterDetailState> = _viewState
    private var characterIdentifier: String? = null

    fun getCharacterById(characterId: String) {
        characterIdentifier = characterId
        viewModelScope.launch {
            _viewState.value = CharacterDetailState.Loading

            try {
                val characterResult = characterDetailUseCase.invoke(characterId)
                _viewState.value = CharacterDetailState.CharacterSuccess(characterResult)
            } catch (e: Exception) {
                _viewState.value = CharacterDetailState.CharacterFailure(e.message)
            }
        }
    }

    fun retry() {
        characterIdentifier?.let {
            getCharacterById(characterId = it)
        }
    }
}