package com.jerryfeng.artistsearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Duration
import java.time.Instant

@Composable
fun FavoritesList(navController: NavController) {
    val favorites by FavoritesManager.favorites.collectAsState()
    if (!LoginService.isLoggedIn) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    navController.navigate("login")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = Dp(30f))
            ) {
                Text("Log in the see favorites")
            }
        }
    } else {
        val isLoading by FavoritesManager.isLoading.collectAsState()
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
        } else if (favorites.isEmpty()) {
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
                    .padding(top = 16.dp)
            ) {
                Text(
                    text="No favorites",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val _now = MutableStateFlow(Instant.now())
            val now by _now.collectAsState()
            LaunchedEffect(Unit) {
                while (true) {
                    delay(1000)
                    _now.value = Instant.now()
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                items(count = favorites.size) { index ->
                    val favorite = favorites[index]
                    Box(modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()) {
                        Column {
                            Text(
                                text = favorite.artistDetail.name,
                                fontSize = TextUnit(20f, TextUnitType.Sp)
                            )
                            Text(
                                text = "${favorite.artistDetail.nationality}, ${favorite.artistDetail.birthYear}",
                                fontSize = TextUnit(14f, TextUnitType.Sp)
                            )
                        }

                        Text(
                            text = getRecency(now, favorite.timestamp),
                            fontSize = TextUnit(14f, TextUnitType.Sp),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 20.dp)
                        )
                        IconButton(
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.CenterEnd),
                            onClick = {
                                navController.navigate("artistInfo/${favorite.artistId}/${favorite.artistDetail.name}")
                            }) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }

}

fun getRecency(now: Instant, timestamp: Long): String {
    val secondsPassed = 1 + Duration.between(Instant.ofEpochMilli(timestamp), now).seconds.toInt()
    if (secondsPassed == 1) {
        return "1 second ago";
    } else if (secondsPassed < 60) {
        return "$secondsPassed seconds ago";
    } else if (secondsPassed < 120) {
        return "1 minute ago";
    } else if (secondsPassed < 3600) {
        return "${secondsPassed/60} minutes ago";
    } else if (secondsPassed < 7200) {
        return "1 hour ago";
    } else if (secondsPassed < 3600 * 24) {
        return "${secondsPassed/3600} hours ago";
    } else if (secondsPassed < 3600 * 24 * 2) {
        return "1 day ago";
    } else {
        return "${(secondsPassed/(3600*24))} days ago";
    }
}
