package com.jerryfeng.artistsearch

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import retrofit2.http.DELETE


private const val BASE_URL =
    "https://csci571-a4.uw.r.appspot.com/api/"

object RetrofitClient {

    private lateinit var _apiService: ApiService
    private var initialized = false

    fun init(context: Context) {
        if (initialized) return

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cookieJar = PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(context)
        )

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .cookieJar(cookieJar)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        _apiService = retrofit.create(ApiService::class.java)
        initialized = true
    }

    val apiService: ApiService
        get() {
            if (!initialized) throw IllegalStateException("RetrofitClient not initialized")
            return _apiService
        }
}

interface ApiService {

    @GET("artists?")
    suspend fun searchArtists(@Query("q") q: String): List<Artist>

    @GET("artists/{artistId}")
    suspend fun getArtist(@Path("artistId") artistId: String): ArtistDetails

    @GET("artworks/{artistId}")
    suspend fun getArtworks(@Path("artistId") artistId: String): List<Artworks>

    @GET("categories/{artworkId}")
    suspend fun getCategories(@Path("artworkId") artworkId: String): List<Category>

    @GET("artists/similar/{artistId}")
    suspend fun getSimilarArtists(@Path("artistId") artistId: String): List<Artist>

    @POST("users")
    suspend fun createUser(@Body registration: Registration): User

    @POST("login")
    suspend fun login(@Body credentials: Credentials): User

    @GET("me")
    suspend fun me(): User

    @GET("logout")
    suspend fun logout()

    @DELETE("me")
    suspend fun deleteUser()

}
