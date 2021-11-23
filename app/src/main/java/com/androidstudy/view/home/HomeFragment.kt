package com.androidstudy.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidstudy.databinding.FragmentHomeBinding
import com.androidstudy.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeBinding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val characterAdapter = CharacterAdapter()

    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(homeBinding.rvItems) {
            apply {
                visibility = View.VISIBLE
                adapter = characterAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        loadCharacters()
    }

    private fun loadCharacters() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.charactersList.collectLatest { item ->
                characterAdapter.submitData(item)
            }
        }

        lifecycleScope.launch {
            characterAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect {  homeBinding.rvItems.scrollToPosition(0) }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}