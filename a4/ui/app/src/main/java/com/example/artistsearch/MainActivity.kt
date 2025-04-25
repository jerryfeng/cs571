package com.example.artistsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.artistsearch.ui.theme.ArtistSearchTheme
import androidx.compose.foundation.layout.Box
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.toRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ArtistSearchTheme {
                ArtistSearchApp()
            }
        }

    }
}




@Composable
fun ArtistSearchApp() {
    val navController = rememberNavController()

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
    }
}
