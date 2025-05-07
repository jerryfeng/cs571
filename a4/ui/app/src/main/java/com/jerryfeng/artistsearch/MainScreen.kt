package com.jerryfeng.artistsearch

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Text("Artist Search")
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("search")
                    }) {
                        Icon(Icons.Outlined.Search, contentDescription = null)
                    }
                    if (LoginService.isLoggedIn) {
                        var expanded by remember { mutableStateOf(false) }
                        IconButton(onClick = {
                            expanded = !expanded
                        }) {
                            Image(
                                painter = rememberAsyncImagePainter(LoginService.user.value.avatar),
                                contentDescription = LoginService.user.value.fullname,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false}
                        ) {
                            DropdownMenuItem(
                                text = { Text(text="Log out")},
                                onClick = {
                                    runBlocking {
                                        LoginService.logout()
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text="Delete account", color = Color.Red)},
                                onClick = {
                                    runBlocking {
                                        LoginService.deleteUser()
                                    }
                                }
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            navController.navigate("login")
                        }) {
                            Icon(Icons.Outlined.Person, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(Dp(10f)),
                text = SimpleDateFormat("dd MMMM yyyy").format(Calendar.getInstance().time)
            )
            Text(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
                    .fillMaxWidth(),
                text = "Favorites",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = TextUnit(22f, TextUnitType.Sp)
            )
            FavoritesList(navController)

            val context = LocalContext.current
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dp(40f))
                    .clickable(onClick = {
                        CustomTabsIntent.Builder().build().launchUrl(
                            context,
                            "https://www.artsy.net/".toUri()
                        )
                    }),
                text = "Powered by Artsy",
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


        }
    }
}
