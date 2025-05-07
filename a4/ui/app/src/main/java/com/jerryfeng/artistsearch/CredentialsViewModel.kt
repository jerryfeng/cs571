package com.jerryfeng.artistsearch

import androidx.compose.material3.Snackbar
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import retrofit2.HttpException
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CredentialsViewModel: ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    private val _nameSupportingText = MutableStateFlow("")
    val nameSupportingText: StateFlow<String> = _nameSupportingText
    private var _nameFocused = false

    fun onNameChanged(newName: String) {
        _name.value = newName
    }

    fun onNameFocusChange(focusState: FocusState) {
        if (!_nameFocused and focusState.isFocused) {
            _nameFocused = true
        } else if (_nameFocused and !focusState.isFocused) {
            if (_name.value.isEmpty()) {
                _nameSupportingText.value = "Full name cannot be empty"
            } else {
                _nameSupportingText.value = ""
            }
        }
    }

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    private val _emailSupportingText = MutableStateFlow("")
    val emailSupportingText: StateFlow<String> = _emailSupportingText
    private var _emailFocused = false

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onEmailFocusChange(focusState: FocusState) {
        if (!_emailFocused and focusState.isFocused) {
            _emailFocused = true
        } else if (_emailFocused and !focusState.isFocused) {
            if (_email.value.isEmpty()) {
                _emailSupportingText.value = "Email cannot be empty"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
                _emailSupportingText.value = "Invalid email format"
            } else {
                _emailSupportingText.value = ""
            }
        }
    }

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _passwordSupportingText = MutableStateFlow("")
    val passwordSupportingText: StateFlow<String> = _passwordSupportingText
    private var _passwordFocused = false

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onPasswordFocusChange(focusState: FocusState) {
        if (!_passwordFocused and focusState.isFocused) {
            _passwordFocused = true
        } else if (_passwordFocused and !focusState.isFocused) {
            if (_password.value.isEmpty()) {
                _passwordSupportingText.value = "Password cannot be empty"
            } else {
                _passwordSupportingText.value = ""
            }
        }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun register(navController: NavController) {
        viewModelScope.launch {
            try {
                if (
                    _nameSupportingText.value.isEmpty() and
                    _emailSupportingText.value.isEmpty() and
                    _passwordSupportingText.value.isEmpty()
                ) {
                    _message.value = ""
                    _isLoading.value = true
                    val user = RetrofitClient.apiService.createUser(Registration(
                        email = _email.value,
                        password = _password.value,
                        fullname = name.value)
                    )
                    LoginService.setUser(user)
                    navController.navigate("main")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, RegistrationError::class.java).message
                if (errorMessage.contains(other = "email", ignoreCase = true)) {
                    _emailSupportingText.value = errorMessage
                } else {
                    _message.value = errorMessage
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(navController: NavController) {
        viewModelScope.launch {
            try {
                if (
                    _emailSupportingText.value.isEmpty() and
                    _passwordSupportingText.value.isEmpty()
                ) {
                    _message.value = ""
                    _isLoading.value = true
                    val user = RetrofitClient.apiService.login(Credentials(
                        email = _email.value,
                        password = _password.value
                    ))
                    LoginService.setUser(user)
                    SnackbarManager.showMessage("Logged in successfully")
                    navController.navigate("main")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, RegistrationError::class.java).message
                _message.value = errorMessage
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
