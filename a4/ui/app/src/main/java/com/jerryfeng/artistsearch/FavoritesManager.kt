package com.jerryfeng.artistsearch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object FavoritesManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> = _favorites
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        scope.launch {
            getFavorite()
            LoginService.loginEvent.collect {
                getFavorite()
            }
        }
    }

    private suspend fun getFavorite() {
        try {
            if (LoginService.isLoggedIn) {
                _isLoading.value = true
                _favorites.value = RetrofitClient.apiService.getFavorites()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _isLoading.value = false
        }
    }

    fun addFavorite(artistId: String) {
        scope.launch {
            try {
                RetrofitClient.apiService.addFavorite(ArtistId(artistId))
                getFavorite()
                SnackbarManager.showMessage("Added to Favorites")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteFavorite(artistId: String) {
        scope.launch {
            try {
                RetrofitClient.apiService.deleteFavorite(ArtistId(artistId))
                getFavorite()
                SnackbarManager.showMessage("Removed from Favorites")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}