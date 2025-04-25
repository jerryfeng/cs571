package com.example.artistsearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistInfoScreen (navController: NavController, artistId: String?, artistTitle: String?) {
    Column {
        TopAppBar(
            colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.Black,
                actionIconContentColor = Color.Gray
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
            }
        )

        var selectedTabIndex by remember { mutableIntStateOf(0) }

        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            Tab(
                selected = (selectedTabIndex == 0),
                onClick = { selectedTabIndex = 0 },
                text = { Text("Details") },
                icon = { Icon(Icons.Outlined.Info, contentDescription = null) }
            )
            Tab(
                selected = (selectedTabIndex == 1),
                onClick = { selectedTabIndex = 1 },
                text = { Text("Artworks") },
                icon = { Icon(Icons.Outlined.AccountBox, contentDescription = null) }
            )
        }

        if (selectedTabIndex == 0) {
            ArtistDetailsTab("$artistId")
        }
    }
}