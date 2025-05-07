package com.jerryfeng.artistsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

class SearchViewModel : ViewModel() {
    private val _data = MutableStateFlow<List<Artist>>(emptyList())
    val data: StateFlow<List<Artist>> = _data

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _noResults = MutableStateFlow(false)
    val noResults: StateFlow<Boolean> = _noResults

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
            _noResults.value = false
            _data.value = RetrofitClient.apiService.searchArtists(q)
            if (_data.value.isEmpty()) _noResults.value = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
