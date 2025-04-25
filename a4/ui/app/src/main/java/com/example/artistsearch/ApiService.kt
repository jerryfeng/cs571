package com.example.artistsearch

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Path


private const val BASE_URL =
    "https://csci571-a4.uw.r.appspot.com/api/"



object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // or BASIC if you want less
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService {

    @GET("artists?")
    suspend fun searchArtists(@Query("q") q: String): List<Artist>

    @GET("artists/{artistId}")
    suspend fun getArtist(@Path("artistId") artistId: String): ArtistDetails

}
