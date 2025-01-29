package com.example.hospital.ui.auth

import AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.ui.home.RemoteViewModel
import androidx.navigation.NavController
import com.example.hospital.R
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val registerResult = viewModel.registerResult.collectAsState().value

    // Handle registration result
    LaunchedEffect(registerResult) {
        when (registerResult) {
            is RegisterResult.Success -> {
                scope.launch {
                    val result =
                        snackbarHostState.showSnackbar(
                            message = "Successfully registered!",
                            duration = SnackbarDuration.Short
                        )
                    if (result == SnackbarResult.Dismissed ||
                        result == SnackbarResult.ActionPerformed
                    ) {
                        navController.navigate("login") { popUpTo("register") { inclusive = true } }
                    }
                }
            }

            is RegisterResult.Failure -> {
                errorMessage = registerResult.error
            }

            else -> Unit // Do nothing for other states
        }
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
            value = user,
            onValueChange = { user = it },
            label = { Text("User") },
            modifier = Modifier.width(350.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(350.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(350.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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

                    viewModel.username = user
                    viewModel.password = password

                    viewModel.register(user, password)
                }
            },
            modifier = Modifier
                .width(350.dp)
                .padding(vertical = 5.dp),
            colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) { Text("Register") }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text(
                text = "Back to Login",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }

    // Display the snackbar at the bottom of the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) { SnackbarHost(hostState = snackbarHostState) }
}
