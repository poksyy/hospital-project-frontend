package com.example.hospital.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo in the center with title below
        Image(
            painter = painterResource(id = R.drawable.hospital_logo),
            contentDescription = "Hospital Logo",
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hospital Management",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to Hospital Management",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )


        // User field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.width(350.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Password field
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
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )

        ) {
            Text("Log In")
        }
    }
}

