package com.jerryfeng.artistsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen (navController: NavController, searchViewModel: SearchViewModel = viewModel()) {
    val artists by searchViewModel.data.collectAsState()
    val query by searchViewModel.query.collectAsState()

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
                    onQueryChange = { searchViewModel.onQueryChanged(it) },
                    onSearch = { searchViewModel.onQueryChanged(it) },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text("Search artists...") },
                    leadingIcon = @Composable { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = @Composable {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                searchViewModel.onQueryChanged("")
                            }
                        )
                    }
                )
            },
            expanded = false,
            onExpandedChange = { },

        ) { }

        ArtistsList(navController, artists)
    }
}
