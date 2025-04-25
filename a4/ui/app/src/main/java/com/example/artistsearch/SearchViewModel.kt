package com.example.artistsearch

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

class SearchViewModel : ViewModel() {
    private val _data = MutableStateFlow<List<Artist>>(emptyList())
    val data: StateFlow<List<Artist>> = _data

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    init {
        viewModelScope.launch {
            _query
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest{ q->
                    if (q.length >= 3) {
                        searchArtists(q)
                    } else {
                        _data.value = emptyList()
                    }
                }
        }
    }

    private suspend fun searchArtists(q: String) {
        try {
            _data.value = RetrofitClient.apiService.searchArtists(q)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
