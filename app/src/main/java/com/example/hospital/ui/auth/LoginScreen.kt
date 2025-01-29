package com.example.hospital.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.hospital.R

@Composable
fun LoginScreen(
        authViewModel: AuthViewModel,
        onNavigateToRegister: () -> Unit,
        onLoginResult: (Boolean) -> Unit
) {
        var username by remember { mutableStateOf(authViewModel.username) }
        var password by remember { mutableStateOf(authViewModel.password) }

        val loginResult by authViewModel.loginResult.collectAsState()
        var errorMessage by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(loginResult) {
                when (loginResult) {
                        is LoginResult.Success -> {
                                errorMessage = null
                                onLoginResult(true)
                        }
                        is LoginResult.Failure -> {
                                errorMessage = (loginResult as LoginResult.Failure).error
                        }
                        else -> {}
                }
        }

        Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Spacer(modifier = Modifier.height(125.dp))

                Image(
                        painter = painterResource(id = R.drawable.hospital_logo),
                        contentDescription = "Hospital Logo",
                        modifier = Modifier.size(250.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                        text = "Hospital Management",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                        text = "Welcome to Hospital Management",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.width(350.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.width(350.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                        onClick = {
                                authViewModel.username = username
                                authViewModel.password = password
                                authViewModel.login()
                        },
                        modifier = Modifier.width(350.dp).padding(vertical = 5.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Color.White
                                )
                ) { Text("Log In") }

                if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                                text = errorMessage!!,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                        )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onNavigateToRegister) {
                        Text(
                                text = "Don't have an account? Sign up",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                        )
                }
        }
}
