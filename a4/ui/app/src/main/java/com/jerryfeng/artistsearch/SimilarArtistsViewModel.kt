package com.jerryfeng.artistsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SimilarArtistsViewModel : ViewModel() {
    private val _artistId = MutableStateFlow("")
    fun setArtistId(artistId: String) {
        _artistId.value = artistId
    }

    private val _data = MutableStateFlow<List<Artist>>(emptyList())
    val data: StateFlow<List<Artist>> = _data

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _artistId.collect { artistId ->
                try {
                    if (artistId != "") {
                        _isLoading.value = true
                        _data.value = RetrofitClient.apiService.getSimilarArtists(artistId)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}