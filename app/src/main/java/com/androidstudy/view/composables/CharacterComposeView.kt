package com.androidstudy.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.androidstudy.R
import com.androidstudy.service.domain.CharacterModel

@Composable
fun CharacterView(modifier: Modifier = Modifier, model: CharacterModel, onClicked: (String) ->  Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClicked.invoke(model.id.toString()) }
            )
    ) {
        val imageModifier = modifier
            .padding(dimensionResource(id = R.dimen.margin_small))
            .clip(shape = RoundedCornerShape(50))

        Image(
            painter = rememberImagePainter(model.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
                .width(72.dp)
                .height(72.dp)
        )

        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.margin_small),
                    top = dimensionResource(id = R.dimen.margin_normal),
                    end = dimensionResource(id = R.dimen.margin_small)
                ),
            text = model.name,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun CharacterComposeView(characters: List<CharacterModel>, onClicked: (String) ->  Unit) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = dimensionResource(id = R.dimen.margin_small),
            top = dimensionResource(id = R.dimen.margin_normal),
            bottom = dimensionResource(id = R.dimen.margin_normal),
            end = dimensionResource(id = R.dimen.margin_small))
    ) {
        items(characters) { item ->
            CharacterView(model = item) { id -> onClicked.invoke(id) }

            Divider(
                Modifier.padding(
                    start = dimensionResource(id = R.dimen.margin_small),
                    end = dimensionResource(id = R.dimen.margin_small)
                ),
                color = Color.LightGray,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewCharactersComposeView() {
    val list = listOf(
        CharacterModel(1, "TEST", "https://image.com"),
        CharacterModel(1, "TEST", "https://image.com")
    )
    CharacterComposeView(characters = list) { }
}

@Composable
@Preview(showBackground = true)
private fun PreviewCharacterView() {
    val list = CharacterModel(1, "TEST", "https://image.com")
    CharacterView(model = list) { }
}