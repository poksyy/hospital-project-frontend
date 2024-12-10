package com.example.hospital.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hospital.R

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginResult: (Boolean) -> Unit
) {
    // Variables to hold username and password inputs
    var username by remember { mutableStateOf(loginViewModel.username) }
    var password by remember { mutableStateOf(loginViewModel.password) }
    var navigateToRegister by remember { mutableStateOf(false) }

    // Navigate to RegisterScreen if 'Sign Up' is clicked
    if (navigateToRegister) {
        // Navigate to RegisterScreen
        RegisterScreen(onBackPressed = { navigateToRegister = false })
    } else {
        // Observe login result state
        val loginResult by loginViewModel.loginResult.collectAsState()

        LaunchedEffect(loginResult) {
            // Handle login result
            when (loginResult) {
                is LoginResult.Success -> onLoginResult(true)
                is LoginResult.Failure -> onLoginResult(false)
                is LoginResult.None -> { /* Do nothing */ }
            }
        }

        // Layout container for the login screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(125.dp))

            // Logo at the top of the screen
            Image(
                painter = painterResource(id = R.drawable.hospital_logo),
                contentDescription = "Hospital Logo",
                modifier = Modifier.size(250.dp)
            )

            // Add spacing between logo and title
            Spacer(modifier = Modifier.height(32.dp))

            // Main title of the app
            Text(
                text = "Hospital Management",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Add small space between the title and the description
            Spacer(modifier = Modifier.height(8.dp))

            // Description or welcoming message
            Text(
                text = "Welcome to Hospital Management",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Username input field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.width(350.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.width(350.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Login button
            Button(
                onClick = {
                    loginViewModel.username = username
                    loginViewModel.password = password
                    loginViewModel.login()
                },
                modifier = Modifier
                    .width(350.dp)
                    .padding(vertical = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Log In")
            }

            // Add space before the text button
            Spacer(modifier = Modifier.height(16.dp))

            // TextButton that navigates to the Register screen
            TextButton(onClick = { navigateToRegister = true }) {
                Text(
                    text = "Don't have an account? Sign up",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
