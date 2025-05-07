package com.jerryfeng.artistsearch

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SnackbarManager {
    private var _snackbarHostState: SnackbarHostState? = null
    private var _scope: CoroutineScope? = null

    fun init(snackbarHostState: SnackbarHostState, scope: CoroutineScope) {
        _snackbarHostState = snackbarHostState
        _scope = scope
    }

    fun showMessage(message: String) {
        _scope?.launch {
            _snackbarHostState?.showSnackbar(message)
        }
    }
//
//    fun showMessageWithAction(message: String, actionLabel: String, onAction: () -> Unit) {
//        _scope?.launch {
//            val result = _snackbarHostState?.showSnackbar(message, actionLabel)
//            if (result == SnackbarResult.ActionPerformed) {
//                onAction()
//            }
//        }
//    }
}