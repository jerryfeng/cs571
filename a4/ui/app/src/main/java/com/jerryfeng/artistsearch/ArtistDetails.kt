package com.jerryfeng.artistsearch

data class ArtistDetails(
    val name: String,
    val birthYear: String,
    val deathYear: String,
    val nationality: String,
    val biography: List<String>
)
