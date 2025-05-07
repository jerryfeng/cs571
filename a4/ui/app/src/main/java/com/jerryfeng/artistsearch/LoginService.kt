package com.jerryfeng.artistsearch

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableSharedFlow

object LoginService {
    private val _user = mutableStateOf(User("","",""))
    val isLoggedIn by derivedStateOf { _user.value.email.isNotEmpty() }
    val user : State<User> = _user
    val loginEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    suspend fun loginFromCookie() {
        try {
            setUser(RetrofitClient.apiService.me())
        } catch (e: Exception) {
            // it is okay to fail when not logged in
        }
    }

    suspend fun setUser(user: User) {
        _user.value = user
        loginEvent.emit(Unit)
    }

    suspend fun logout() {
        try {
            RetrofitClient.apiService.logout()
            setUser(User("","",""))
            SnackbarManager.showMessage("Logged out successfully")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteUser() {
        try {
            RetrofitClient.apiService.deleteUser()
            setUser(User("","",""))
            SnackbarManager.showMessage("Deleted user successfully")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
