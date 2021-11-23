package com.androidstudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.androidstudy.repository.CharacterRepository
import com.androidstudy.service.domain.CharacterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: CharacterRepository) : ViewModel() {

    val charactersList: Flow<PagingData<CharacterModel>> = repo.getCharacters().cachedIn(viewModelScope)
}
