package com.androidstudy.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.androidstudy.view.composables.CharacterDetailView
import com.androidstudy.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getCharacterById(intent.getStringExtra(ARG_CHARACTER_ID) ?: "")

        setContent {
            CharacterDetailView(
                viewState = viewModel.viewState,
                backAction = { onBackPressed() },
                onRetry = { viewModel.retry() }
            )
        }
    }

    companion object {

        private const val ARG_CHARACTER_ID = "ARG_CHARACTER_ID"

        fun newInstance(context: Context, postId: String): Intent {
            return Intent(context, DetailActivity::class.java)
                .apply {
                    putExtra(ARG_CHARACTER_ID, postId)
                }
        }

    }
}