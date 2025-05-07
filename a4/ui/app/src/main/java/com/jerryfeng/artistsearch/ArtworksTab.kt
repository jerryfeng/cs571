package com.jerryfeng.artistsearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.runtime.remember

@Composable
fun ArtworksTab(artistId: String, artworksViewModel: ArtworksViewModel = viewModel()) {
    artworksViewModel.setArtistId(artistId)
    val isLoading by artworksViewModel.isLoading.collectAsState()
    val artworkId = remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }

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
        val artworks by artworksViewModel.data.collectAsState()
        if (artworks.isEmpty()) {
            Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text="No Artworks",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(count = artworks.size) { index ->
                    val artwork = artworks[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(artwork.href),
                            contentDescription = artwork.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(540.dp)
                                .background(Color(0xFFEEEEEE)),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "${artwork.title}, ${artwork.year}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            fontSize = TextUnit(18f, TextUnitType.Sp),
                            fontWeight = FontWeight.Medium
                        )
                        Button(
                            onClick = {
                                artworkId.value = artwork.id
                                openDialog.value = true
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 16.dp)
                        ) {
                            Text("View categories")
                        }
                    }
                }
            }
        }

        if (openDialog.value) {
            CategoriesDialog(artworkId.value, { openDialog.value = false })
        }
    }
}
