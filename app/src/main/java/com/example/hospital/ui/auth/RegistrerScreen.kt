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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.R

@Composable
fun RegisterScreen(
    // Navigates back to the LoginScreen.
    onNavigateBack: () -> Unit,
    // HomeScreen redirection.
    onRegisterSuccess: () -> Unit,
    // Allows data export from AuthViewModel.
    viewModel: AuthViewModel = viewModel()
) {
    var confirmPassword by remember { mutableStateOf("") }

    // Observes the registerResult state from the AuthViewModel and updates the UI reactively when it changes.
    val registerResult by viewModel.registerResult.collectAsState()

    // Effect to handle successful registration and navigate to home screen.
    LaunchedEffect(registerResult) {
        when (registerResult) {
            is AuthUiState.RegisterResult.Success -> {
                viewModel.resetRegisterState()
                onRegisterSuccess()
            }
            else -> {}
        }
    }

    // Effect to reset the register state when entering the screen.
    // This is going to execute every time the user enters the RegisterScreen.
    LaunchedEffect(Unit) {
        viewModel.resetRegisterState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
            text = "Create a new account",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            // Username value entered by the user.
            value = viewModel.username,
            // Handles username input change by updating the ViewModel.
            onValueChange = { viewModel.username = it },
            label = { Text("Username") },
            modifier = Modifier.width(350.dp),
            // Disables the field while registration is in progress.
            enabled = registerResult !is AuthUiState.RegisterResult.Loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            // Password value entered by the user.
            value = viewModel.password,
            // Handles password input change by updating the ViewModel.
            onValueChange = { viewModel.password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(350.dp),
            // Disables the field while registration is in progress.
            enabled = registerResult !is AuthUiState.RegisterResult.Loading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            // Confirm password value entered by the user.
            value = confirmPassword,
            // Handles confirm password input change.
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(350.dp),
            // Disables the field while registration is in progress.
            enabled = registerResult !is AuthUiState.RegisterResult.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Displays error message when registration fails.
        if (registerResult is AuthUiState.RegisterResult.Failure) {
            Text(
                text = (registerResult as AuthUiState.RegisterResult.Failure).error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Button(
            // Trigger registration action, checks if passwords match.
            onClick = {
                if (viewModel.password != confirmPassword) {
                    viewModel._registerResult.value =
                        AuthUiState.RegisterResult.Failure("Passwords do not match")
                }
                viewModel.register(viewModel.username, viewModel.password)
            },
            modifier = Modifier
                .width(350.dp)
                .padding(vertical = 5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            // Disables the button while registration is in progress.
            enabled = registerResult !is AuthUiState.RegisterResult.Loading
        ) {
            // Shows loading indicator while registration is in progress.
            if (registerResult is AuthUiState.RegisterResult.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Register")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                // We make sure that the register form is reset.
                viewModel.resetRegisterState()
                // Navigates back to the LoginScreen.
                onNavigateBack()
            },
            enabled = registerResult !is AuthUiState.RegisterResult.Loading
        ) {
            Text(
                text = "Back to Login",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
