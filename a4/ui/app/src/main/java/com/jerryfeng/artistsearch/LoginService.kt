package com.jerryfeng.artistsearch

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

object LoginService {
    private val _user = mutableStateOf(User("","",""))
    val isLoggedIn by derivedStateOf { _user.value.email.isNotEmpty() }
    val user : State<User> = _user
    fun setUser(user: User) {
        _user.value = user
    }

    suspend fun loginFromCookie() {
        try {
            setUser(RetrofitClient.apiService.me())
        } catch (e: Exception) {
            // it is okay to fail when not logged in
        }
    }

    suspend fun logout() {
        try {
            RetrofitClient.apiService.logout()
            setUser(User("","",""))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteUser() {
        try {
            RetrofitClient.apiService.deleteUser()
            setUser(User("","",""))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
