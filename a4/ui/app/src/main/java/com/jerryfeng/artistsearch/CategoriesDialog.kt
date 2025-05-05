package com.jerryfeng.artistsearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDialog(
    artworkId: String,
    onDismissRequest: () -> Unit,
    categoriesDialogViewModel: CategoriesDialogViewModel = viewModel()
) {
    categoriesDialogViewModel.setArtworkId(artworkId)
    val isLoading by categoriesDialogViewModel.isLoading.collectAsState()

    BasicAlertDialog(onDismissRequest = { onDismissRequest() } ) {
        if (isLoading) {
            Column(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Loading...",
                    fontSize = TextUnit(12f, TextUnitType.Sp)
                )
            }

        } else {
            val categories by categoriesDialogViewModel.data.collectAsState()


            Card(
                colors = CardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Black,
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = "Categories",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    fontSize = TextUnit(28f, TextUnitType.Sp),
                    fontWeight = FontWeight.Medium
                )
                if (categories.isEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No categories available",
                        textAlign = TextAlign.Center
                    )
                } else {
                    Box {
                        val state = rememberCarouselState { categories.size }
                        val scope = rememberCoroutineScope()
                        HorizontalUncontainedCarousel(
                            state = state,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .width(300.dp)
                                .height(450.dp),
                            itemWidth = 242.dp,
                            itemSpacing = 8.dp
                        ) { i ->
                            val category = categories[i]
                            Card(
                                colors = CardColors(
                                    containerColor = Color(0xFFEEEEEE),
                                    contentColor = Color.Black,
                                    disabledContainerColor = Color.Black,
                                    disabledContentColor = Color(0xFFEEEEEE)
                                )
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(category.thumbnail),
                                    contentDescription = category.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = category.name,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp, bottom = 8.dp),
                                    fontSize = TextUnit(24f, TextUnitType.Sp),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = category.description,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp)
                                        .padding(horizontal = 12.dp),
                                    fontSize = TextUnit(14f, TextUnitType.Sp)
                                )
                            }
                        }

                        IconButton(
                            modifier = Modifier.padding(top = 200.dp),
                            onClick = {
                                scope.launch {
                                    state.animateScrollBy(-770f)
                                }
                            }) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = null
                            )
                        }
                        IconButton(
                            modifier = Modifier.padding(top = 200.dp, start = 260.dp),
                            onClick = {
                                scope.launch {
                                    state.animateScrollBy(770f)
                                }
                            }) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                    }
                }
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(24.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
}