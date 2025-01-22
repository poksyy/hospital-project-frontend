package com.example.hospital.ui.auth

import AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.hospital.R

@Composable
fun LoginScreen(
        authViewModel: AuthViewModel,
        onNavigateToRegister: () -> Unit, // Función para navegar a la pantalla de registro
        onLoginResult: (Boolean) -> Unit
) {
    var username by remember { mutableStateOf(authViewModel.username) }
    var password by remember { mutableStateOf(authViewModel.password) }

    val loginResult by authViewModel.loginResult.collectAsState()

    LaunchedEffect(loginResult) {
        when (loginResult) {
            is LoginResult.Success -> onLoginResult(true)
            is LoginResult.Failure -> onLoginResult(false)
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
