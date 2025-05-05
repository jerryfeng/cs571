package com.jerryfeng.artistsearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter

private const val MISSING_IMAGE_URL = "/assets/shared/missing_image.png"

@Composable
fun ArtistsList(navController: NavController, artists: List<Artist>) {
    LazyColumn {
        items(count = artists.size) { index ->
            val result = artists[index]
            val painter =
                if (result.thumbnail != MISSING_IMAGE_URL)
                    rememberAsyncImagePainter(result.thumbnail)
                else
                    painterResource(id = R.drawable.ic_launcher_foreground)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(214.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        navController.navigate("ArtistInfo/${result.id}/${result.title}")
                    }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painter,
                        contentDescription = result.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFEEEEEE)),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = result.title,
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
                }
            }
        }
    }
}