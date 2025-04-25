package com.example.artistsearch

import android.graphics.Paint.Align
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navArgument
import coil3.compose.rememberAsyncImagePainter

private const val MISSING_IMAGE_URL = "/assets/shared/missing_image.png"
private const val MISSING_IMAGE_REPLACEMENT_URL = "/public/artsy_logo.svg"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen (navController: NavController, viewModel: SearchViewModel = viewModel()) {
    val results by viewModel.data.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }

    Column {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer
                ),
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer
                        ),
                    query = query,
                    onQueryChange = {
                        query = it
                        viewModel.onQueryChanged(it)
                    },
                    onSearch = { viewModel.onQueryChanged(it) },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("Search artists...") },
                    leadingIcon = @Composable { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = @Composable {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                query = ""
                                viewModel.onQueryChanged("")
                            }
                        )
                    }
                )
            },
            expanded = false,
            onExpandedChange = { },

        ) { }

        // Display search results in a scrollable column
        LazyColumn {
            items(count = results.size) { index ->
                val result = results[index]
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
}
