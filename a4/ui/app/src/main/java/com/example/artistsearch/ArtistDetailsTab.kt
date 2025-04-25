package com.example.artistsearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ArtistDetailsTab(artistId: String, artistDetailsViewModel: ArtistDetailsViewModel = viewModel()) {
    artistDetailsViewModel.setArtistId(artistId)
    val isLoading by artistDetailsViewModel.isLoading.collectAsState()

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
        val artistDetail by artistDetailsViewModel.data.collectAsState()
        LazyColumn {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    text = "${artistDetail?.name}",
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(32f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    text = "${artistDetail?.nationality}, ${artistDetail?.birthYear} - ${artistDetail?.deathYear}",
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    fontWeight = FontWeight.Medium
                )
            }
            items(artistDetail?.biography ?: emptyList()) {paragraph ->
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        text = paragraph,
                        textAlign = TextAlign.Justify
                    )
            }
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = ""
                )
            }
        }
    }

}