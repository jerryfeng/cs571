package com.jerryfeng.artistsearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, credentialsViewModel: CredentialsViewModel = viewModel()) {
    Column {
        TopAppBar(
            colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            ),
            title = {
                Row {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                    Text(
                        text = "Login",
                        modifier = Modifier
                            .padding(top = 10.dp, start = 6.dp)
                    )
                }
            }
        )
        Row(modifier = Modifier.fillMaxHeight()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val email by credentialsViewModel.email.collectAsState()
                val emailSupportingText by credentialsViewModel.emailSupportingText.collectAsState()
                val password by credentialsViewModel.password.collectAsState()
                val passwordSupportingText by credentialsViewModel.passwordSupportingText.collectAsState()
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        credentialsViewModel.onEmailChanged(it)
                    },
                    label = @Composable { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .onFocusChanged{ credentialsViewModel.onEmailFocusChange(it) },
                    supportingText = @Composable {
                        if (emailSupportingText.isNotEmpty()) {
                            Text(text=emailSupportingText)
                        }
                    },
                    isError = emailSupportingText.isNotEmpty()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        credentialsViewModel.onPasswordChanged(it)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    label = @Composable { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .onFocusChanged { credentialsViewModel.onPasswordFocusChange(it) },
                    supportingText = @Composable {
                        if (passwordSupportingText.isNotEmpty()) {
                            Text(text=passwordSupportingText)
                        }
                    },
                    isError = passwordSupportingText.isNotEmpty()
                )
                val isLoading by credentialsViewModel.isLoading.collectAsState()
                Button(
                    onClick = {
                        credentialsViewModel.login(navController)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Login")
                    }
                }
                val message by credentialsViewModel.message.collectAsState()
                if (message.isNotEmpty()) {
                    Text(
                        text= message,
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(horizontal = 16.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text="Don't have an account yet? ")
                    TextButton(
                        onClick = {
                            navController.navigate("register")
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text="Register")
                    }
                }
            }
        }
    }
}
