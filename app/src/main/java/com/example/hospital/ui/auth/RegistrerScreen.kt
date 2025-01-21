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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.ui.home.RemoteViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onBackPressed: () -> Unit,
    viewModel: RemoteViewModel = viewModel()
) {
    // Variables to hold username, password, and confirm password inputs.
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    // Layout container for the registration screen.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(125.dp))

        // Logo at the top of the screen.
        Image(
            painter = painterResource(id = R.drawable.hospital_logo),
            contentDescription = "Hospital Logo",
            modifier = Modifier.size(250.dp)
        )

        // Add spacing between logo and title.
        Spacer(modifier = Modifier.height(32.dp))

        // Main title of the app.
        Text(
            text = "Hospital Management",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Add small space between the title and the description.
        Spacer(modifier = Modifier.height(8.dp))

        // Description or registration message.
        Text(
            text = "Create a new account",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Username input field.
        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text("User") },
            modifier = Modifier.width(350.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password input field.
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(350.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password input field.
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(350.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Register button.
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                scope.launch {
                    if (password != confirmPassword) {
                        errorMessage = "Passwords don't match"
                        return@launch
                    }

                    viewModel.postRegistration(user, password)
                        .onSuccess {
                            errorMessage = null
                            onBackPressed()
                        }
                        .onFailure {
                            errorMessage = it.message ?: "Registration failed"
                        }
                }
            },
            modifier = Modifier
                .width(350.dp)
                .padding(vertical = 5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TextButton that navigates back to the Login screen.
        TextButton(onClick = onBackPressed) {
            Text(
                text = "Back to Login",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}