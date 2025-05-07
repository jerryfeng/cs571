package com.jerryfeng.artistsearch

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jerryfeng.artistsearch.ui.theme.ArtistSearchTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.init(this)
        lifecycleScope.launch {
            LoginService.loginFromCookie()
        }

        enableEdgeToEdge()

        setContent {
            ArtistSearchTheme {
                ArtistSearchApp()
            }
        }

    }
}




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArtistSearchApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        SnackbarManager.init(snackbarHostState, coroutineScope)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        content = {
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    MainScreen(navController)
                }
                composable("search") {
                    SearchScreen(navController)
                }
                composable("artistInfo/{artistId}/{artistTitle}") {
                    val artistId = it.arguments?.getString("artistId");
                    val artistTitle = it.arguments?.getString("artistTitle");
                    ArtistInfoScreen(navController, artistId, artistTitle)
                }
                composable("login") {
                    LoginScreen(navController)
                }
                composable("register") {
                    RegisterScreen(navController)
                }
            }
        }
    )
}
