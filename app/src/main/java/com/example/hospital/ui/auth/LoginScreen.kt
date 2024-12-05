package com.example.hospital.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hospital.R

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, onLoginResult: (Boolean) -> Unit) {
    // Observe the loginResult flow from the ViewModel
    val loginResult by loginViewModel.loginResult.collectAsState()

    // Connect the text fields to the ViewModel properties
    var username by remember { mutableStateOf(loginViewModel.username) }
    var password by remember { mutableStateOf(loginViewModel.password) }

    // Update the UI based on the loginResult
    LaunchedEffect(loginResult) {
        when (loginResult) {
            is LoginResult.Success -> onLoginResult(true)
            is LoginResult.Failure -> onLoginResult(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        // Title with image
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cross),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Welcome to the Nurse Login",
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // User field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Connect the login button to the ViewModel's login() method
        Button(
            onClick = {
                loginViewModel.username = username
                loginViewModel.password = password
                loginViewModel.login()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.Red, contentColor = Color.White
            )
        ) {
            Text("Log In")
        }
    }
}
