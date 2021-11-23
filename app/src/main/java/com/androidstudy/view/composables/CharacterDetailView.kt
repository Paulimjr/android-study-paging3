package com.androidstudy.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.androidstudy.R
import com.androidstudy.service.domain.CharacterDetailState
import com.androidstudy.service.domain.CharacterModel
import com.androidstudy.view.errorView.ErrorView
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CharacterDetailView(
    viewState: StateFlow<CharacterDetailState>,
    backAction: () -> Unit,
    onRetry: () -> Unit
) {
    val introViewState: State<CharacterDetailState> = viewState.collectAsState()

    when (val stateValue = introViewState.value) {

        is CharacterDetailState.Loading -> LoadingScreen()

        is CharacterDetailState.CharacterSuccess -> {
            stateValue.result?.let { model ->
                CharacterInfoView(
                    model = model,
                    backAction = { backAction.invoke() }
                )
            }
        }
        is CharacterDetailState.CharacterFailure -> ShowErrorView(
            onRetry = { onRetry.invoke() }
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CharacterInfoView(modifier: Modifier = Modifier, model: CharacterModel, backAction: () -> Unit) {
    Scaffold(
        topBar = {
            SimpleSolidToolbar(model.name) { backAction.invoke() }
        },
        content = {
            Column(modifier = modifier.fillMaxWidth()) {

                Image(
                    painter = rememberImagePainter(model.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier.fillMaxWidth()
                )
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.margin_small),
                            top = dimensionResource(id = R.dimen.margin_normal),
                            bottom = dimensionResource(id = R.dimen.margin_normal),
                            end = dimensionResource(id = R.dimen.margin_small)
                        ),
                    text = "Character name: ${model.name}",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    )
}


@Composable
fun SimpleSolidToolbar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 64.dp),
                text = title.toUpperCase(Locale.current),
                textAlign = TextAlign.Center,
            )
        },
        backgroundColor = Color.White,
        contentColor = Color.Black,
        navigationIcon = {
            IconButton(onClick = { onBack.invoke() }) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun ShowErrorView(modifier: Modifier = Modifier, onRetry: () -> Unit) {
    AndroidView(
        modifier = modifier.fillMaxWidth()
            .fillMaxHeight(),
        factory = { ctx ->
            ErrorView(context = ctx).apply {
                this.setOnRetryListener { onRetry.invoke() }
            }
        }
    )
}