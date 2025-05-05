package com.jerryfeng.artistsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
    }
}
