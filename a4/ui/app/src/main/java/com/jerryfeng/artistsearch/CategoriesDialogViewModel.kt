package com.jerryfeng.artistsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesDialogViewModel : ViewModel() {
    private val _artworkId = MutableStateFlow("")
    fun setArtworkId(artworkId: String) {
        _artworkId.value = artworkId
    }

    private val _data = MutableStateFlow<List<Category>>(emptyList())
    val data: StateFlow<List<Category>> = _data

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _artworkId.collect { artworkId ->
                try {
                    if (artworkId != "") {
                        _isLoading.value = true
                        _data.value = RetrofitClient.apiService.getCategories(artworkId)
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