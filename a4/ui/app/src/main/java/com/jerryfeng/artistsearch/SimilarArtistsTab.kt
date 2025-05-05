package com.jerryfeng.artistsearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SimilarArtistsTab(
    artistId: String,
    navController: NavController,
    similarArtistsViewModel: SimilarArtistsViewModel = viewModel()
) {
    similarArtistsViewModel.setArtistId(artistId)
    val isLoading by similarArtistsViewModel.isLoading.collectAsState()

    if (isLoading) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text="Loading...",
                fontSize = TextUnit(12f, TextUnitType.Sp)
            )
        }

    } else {
        val artists by similarArtistsViewModel.data.collectAsState()
        if (artists.isEmpty()) {
            Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = Color.Black,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text="No Similar Artists",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            ArtistsList(navController, artists)
        }
    }
}