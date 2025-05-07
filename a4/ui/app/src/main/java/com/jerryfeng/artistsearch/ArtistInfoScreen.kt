package com.jerryfeng.artistsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

private enum class ArtistInfoTab {
    ARTIST_DETAIL,
    ARTWORKS,
    SIMILAR_ARTISTS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistInfoScreen (
    navController: NavController,
    artistId: String?,
    artistTitle: String?
) {
    Column {
        TopAppBar(
            colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            ),
            title = {
                Row() {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                    Text(
                        text = artistTitle?: "",
                        modifier = Modifier
                            .padding(top = 10.dp, start = 6.dp)
                    )
                }
            },
            actions = {
                if (LoginService.isLoggedIn) {
                    val id = "$artistId"
                    val favorites by FavoritesManager.favorites.collectAsState()
                    val favoritesId = remember { derivedStateOf { favorites.map { favorite: Favorite -> favorite.artistId } } }
                    IconButton(
                        onClick = {

                            if (favoritesId.value.contains(id)) {
                                FavoritesManager.deleteFavorite(id)
                            } else {
                                FavoritesManager.addFavorite(id)
                            }
                        },
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(CircleShape)
                            .size(36.dp)
                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        if (favoritesId.value.contains(id)) {
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
        )

        var selectedTab by remember { mutableStateOf(ArtistInfoTab.ARTIST_DETAIL) }

        SecondaryTabRow(
            selectedTabIndex = selectedTab.ordinal
        ) {
            // like seriously, why not just use the default black, or any Material theme color
            val hw4AuthorSpecificallyChosenTabColorOverride = Color(0xFF4863A0)
            Tab(
                selected = (selectedTab == ArtistInfoTab.ARTIST_DETAIL),
                onClick = { selectedTab = ArtistInfoTab.ARTIST_DETAIL },
                text = { Text("Details", color = hw4AuthorSpecificallyChosenTabColorOverride) },
                icon = { Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = hw4AuthorSpecificallyChosenTabColorOverride
                ) }
            )
            Tab(
                selected = (selectedTab == ArtistInfoTab.ARTWORKS),
                onClick = { selectedTab = ArtistInfoTab.ARTWORKS },
                text = { Text("Artworks", color = hw4AuthorSpecificallyChosenTabColorOverride) },
                icon = { Icon(
                    Icons.Outlined.AccountBox,
                    contentDescription = null,
                    tint = hw4AuthorSpecificallyChosenTabColorOverride
                ) }
            )
            if (LoginService.isLoggedIn) {
                Tab(
                    selected = (selectedTab == ArtistInfoTab.SIMILAR_ARTISTS),
                    onClick = { selectedTab = ArtistInfoTab.SIMILAR_ARTISTS },
                    text = { Text("Similar", color = hw4AuthorSpecificallyChosenTabColorOverride) },
                    icon = { Icon(
                        painter = painterResource(id = R.drawable.personsearch),
                        contentDescription = null,
                        tint = hw4AuthorSpecificallyChosenTabColorOverride
                    ) }
                )
            }
        }

        when (selectedTab) {
            ArtistInfoTab.ARTIST_DETAIL -> {
                ArtistDetailsTab("$artistId")
            }
            ArtistInfoTab.ARTWORKS -> {
                ArtworksTab("$artistId")
            }
            ArtistInfoTab.SIMILAR_ARTISTS -> {
                SimilarArtistsTab("$artistId", navController)
            }
        }
    }
}
