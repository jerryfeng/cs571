package com.jerryfeng.artistsearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter

private const val MISSING_IMAGE_URL = "/assets/shared/missing_image.png"

@Composable
fun ArtistsList(navController: NavController, artists: List<Artist>) {
    val favorites by FavoritesManager.favorites.collectAsState()
    val favoritesId = remember { derivedStateOf { favorites.map { favorite: Favorite -> favorite.artistId } } }
    LazyColumn {
        items(count = artists.size) { index ->
            val artist = artists[index]
            val painter =
                if (artist.thumbnail != MISSING_IMAGE_URL)
                    rememberAsyncImagePainter(artist.thumbnail)
                else
                    painterResource(id = R.drawable.ic_launcher_foreground)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(214.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        navController.navigate("ArtistInfo/${artist.id}/${artist.title}")
                    }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painter,
                        contentDescription = artist.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainer),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = artist.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f))
                            .align(Alignment.BottomStart)
                            .padding(vertical = 6.dp, horizontal = 12.dp),
                        fontSize = TextUnit(18f, TextUnitType.Sp),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = ">",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(vertical = 6.dp, horizontal = 12.dp),
                        fontSize = TextUnit(18f, TextUnitType.Sp),
                        fontWeight = FontWeight.Medium
                    )
                    if (LoginService.isLoggedIn) {
                        IconButton(
                            onClick = {
                                if (favoritesId.value.contains(artist.id)) {
                                    FavoritesManager.deleteFavorite(artist.id)
                                } else {
                                    FavoritesManager.addFavorite(artist.id)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(CircleShape)
                                .size(36.dp)
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            if (favoritesId.value.contains(artist.id)) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Toggle favorite",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_star_24),
                                    contentDescription = "Toggle favorite",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}