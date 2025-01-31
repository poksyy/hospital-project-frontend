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
fun LoginScreen(
    // RegisterScreen redirection.
    onNavigateToRegister: () -> Unit,
    // HomeScreen redirection.
    onLoginSuccess: () -> Unit,
    // Allows data export from AuthViewModel.
    viewModel: AuthViewModel = viewModel()
) {
    // Observes the loginResult state from the AuthViewModel and updates the UI reactively when it changes.
    val loginResult by viewModel.loginResult.collectAsState()

    // Effect to handle successful login and navigate to home screen.
    LaunchedEffect(loginResult) {
        when (loginResult) {
            // Store the success to the onLoginSuccess().
            is AuthUiState.LoginResult.Success -> onLoginSuccess()

            else -> {}
        }
    }

    LoginContent(

        // Username value from the AuthViewModel.
        username = viewModel.username,
        // Password value from the AuthViewModel.
        password = viewModel.password,
        // Error message displayed when login fails.
        errorMessage = (loginResult as? AuthUiState.LoginResult.Failure)?.error,
        // Handles username input change by updating the ViewModel.
        onUsernameChange = { viewModel.username = it },
        // Handles password input change by updating the ViewModel.
        onPasswordChange = { viewModel.password = it },
        // Defines the login action triggered when the login button is clicked.
        onLoginClick = { viewModel.login() },
        // Defines the action to navigate to the registration screen.
        onRegisterClick = onNavigateToRegister,
        // Indicates whether login is in progress, disables inputs if true.
        isLoading = loginResult is AuthUiState.LoginResult.Loading
    )
}

@Composable
// Composable function to render the login content UI.
private fun LoginContent(

    // The username entered by the user.
    username: String,
    // The password entered by the user.
    password: String,
    // Error message for login failure.
    errorMessage: String?,
    // Lambda to handle username input change.
    onUsernameChange: (String) -> Unit,
    // Lambda to handle password input change.
    onPasswordChange: (String) -> Unit,
    // Lambda to trigger login action.
    onLoginClick: () -> Unit,
    // Lambda to navigate to register screen.
    onRegisterClick: () -> Unit,
    // Indicates whether the login is in progress.
    isLoading: Boolean,
    // Modifier for styling and layout customization.
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
            text = "Welcome to Hospital Management",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Username") },
            modifier = Modifier.width(350.dp),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(350.dp),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .width(350.dp)
                .padding(vertical = 5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White
            ),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Log In")
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            // Defines the action to navigate to RegisterScreen when the user clicks the button.
            onClick = onRegisterClick, enabled = !isLoading
        ) {
            Text(
                text = "Don't have an account? Sign up",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}