package com.jerryfeng.artistsearch


data class Favorite(
    val email: String,
    val artistId: String,
    val artistDetail: ArtistDetail,
    val timestamp: Long
)

data class ArtistDetail(
    val name: String,
    val birthYear: String,
    val deathYear: String,
    val nationality: String
)