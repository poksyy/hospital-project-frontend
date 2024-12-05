package com.example.hospital.ui.home

import com.example.hospital.ui.nurses.search.SearchScreen
import com.example.hospital.ui.nurses.list.ListScreen
import com.example.hospital.ui.auth.LoginScreen
import com.example.hospital.ui.auth.LoginViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hospital.R
import com.example.hospital.ui.theme.HospitalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HospitalTheme {
                MainScreen(
                    loginViewModel = LoginViewModel() // Provide the LoginViewModel instance
                )
            }
        }
    }
}

// MainScreen: Controls navigation between different screens.
@Composable
fun MainScreen(
    loginViewModel: LoginViewModel
) {
    // Mutable states to track which screen is currently displayed
    var showListScreen by remember { mutableStateOf(false) }  // Show the list screen
    var showSearchScreen by remember { mutableStateOf(false) } // Show the search screen
    var showLoginScreen by remember { mutableStateOf(false) }  // Show the login screen

    // Use a `when` expression to decide which screen to show.
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            showListScreen -> {
                ListScreen()
            }

            showSearchScreen -> {
                SearchScreen()
            }

            showLoginScreen -> {
                // Display the LoginScreen component with a callback for login results.
                LoginScreen(
                    loginViewModel = loginViewModel,
                    onLoginResult = { isSuccess ->
                        if (isSuccess) {
                            // If login is successful, hide the login screen and show the list screen.
                            showLoginScreen = false
                            showListScreen = true
                        }
                    }
                )
            }

            else -> {
                // Default: Display the landing page with a formal and clean UI.
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Optional: Add an image/logo at the top of the screen (if needed)
                    Image(
                        painter = painterResource(id = R.drawable.hospital_logo),  // Replace with your logo resource
                        contentDescription = "Hospital Logo",
                        modifier = Modifier
                            .size(250.dp)
                            .padding(bottom = 20.dp)
                    )

                    Text(
                        text = "Welcome to Hospital Management",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Replacing Card-style buttons with simple Buttons
                    MainScreenButton(
                        text = "Search Nurses",
                        onClick = { showSearchScreen = true }
                    )
                    MainScreenButton(
                        text = "List of Nurses",
                        onClick = { showListScreen = true }
                    )
                    MainScreenButton(
                        text = "Login",
                        onClick = { showLoginScreen = true }
                    )
                }
            }
        }

        // Back button in the bottom right corner
        if (showListScreen || showSearchScreen || showLoginScreen) {
            BackButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp) // Add some padding for aesthetics
            ) {
                // Handle back navigation
                if (showListScreen) {
                    showListScreen = false
                } else if (showSearchScreen) {
                    showSearchScreen = false
                } else if (showLoginScreen) {
                    showLoginScreen = false
                }
            }
        }
    }
}

// Composable for the Back Button
@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier) {
        Text("Back")
    }
}

// Reusable button composable for the main screen
@Composable
fun MainScreenButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(350.dp)
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(13.dp)
    ) {
        Text(text)
    }
}
