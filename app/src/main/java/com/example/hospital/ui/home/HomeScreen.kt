package com.example.hospital.ui.home

import AuthViewModel
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hospital.R
import com.example.hospital.ui.auth.LoginScreen
import com.example.hospital.ui.profile.ProfileScreen
import com.example.hospital.ui.auth.RegisterScreen
import com.example.hospital.ui.nurses.list.ListScreen
import com.example.hospital.ui.nurses.search.SearchScreen
import com.example.hospital.ui.theme.HospitalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HospitalTheme { AppNavigation() } }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var isLoggedIn by remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = if (isLoggedIn) "main" else "login") {
        composable("login") {
            LoginScreen(
                    authViewModel = AuthViewModel(),
                    onNavigateToRegister = { navController.navigate("register") },
                    onLoginResult = { isSuccess ->
                        if (isSuccess) {
                            isLoggedIn = true
                            navController.navigate("main") { popUpTo("login") { inclusive = true } }
                        }
                    }
            )
        }

        composable("register") { RegisterScreen(onBackPressed = { navController.popBackStack() }) }

        composable("main") {
            MainScreen(
                    authViewModel = AuthViewModel(),
                    onLogout = {
                        isLoggedIn = false
                        navController.popBackStack("login", inclusive = false)
                    }
            )
        }

        composable("search") { SearchScreen(onBackPressed = { navController.popBackStack() }) }
        composable("list") { ListScreen(onBackPressed = { navController.popBackStack() }) }
        composable("profile") { ProfileScreen(onBackPressed = { navController.popBackStack() }) }
    }
}

@Composable
fun MainScreen(
        remoteViewModel: RemoteViewModel = viewModel(),
        authViewModel: AuthViewModel,
        onLogout: () -> Unit
) {
    var showListScreen by remember { mutableStateOf(false) }
    var showSearchScreen by remember { mutableStateOf(false) }
    var showUserScreen by remember { mutableStateOf(false) }

    val onBackPressed: () -> Unit = {
        if (showListScreen) {
            showListScreen = false
        } else if (showSearchScreen) {
            showSearchScreen = false
        } else if (showUserScreen) {
            showSearchScreen = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            showListScreen -> {
                ListScreen(onBackPressed = onBackPressed)
            }
            showSearchScreen -> {
                SearchScreen(onBackPressed = onBackPressed)
            }
            showUserScreen -> {
                ProfileScreen(onBackPressed = onBackPressed)
            }
            else -> {
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
                            text = "Manage your hospital tasks with ease",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Buttons for navigating to different screens
                    MainScreenButton(text = "Search Nurses", onClick = { showSearchScreen = true })
                    MainScreenButton(text = "List of Nurses", onClick = { showListScreen = true })
                    MainScreenButton(text = "Profile", onClick = { showUserScreen = true })

                    Spacer(modifier = Modifier.height(16.dp))

                    // Logout button
                    MainScreenButton(
                            text = "Logout",
                            onClick = onLogout,
                            buttonColor = Color(0xFFB71C1C)
                    )

                    LaunchedEffect(Unit) { remoteViewModel.getRemoteNurse() }

                    when (val uiState = remoteViewModel.remoteMessageUiState) {
                        is RemoteMessageUiState.Success -> {
                            val nurses = uiState.remoteMessage.body() ?: emptyList()
                            if (nurses.isNotEmpty()) {
                                Text("Nurse Names: ${nurses.joinToString { it.name ?: "Unknown" }}")
                            } else {
                                Text("No nurse data available")
                            }
                        }
                        is RemoteMessageUiState.Error -> {
                            Text("Error occurred while fetching nurse data")
                        }
                        RemoteMessageUiState.Loading -> {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreenButton(
        text: String,
        onClick: () -> Unit,
        buttonColor: Color = MaterialTheme.colorScheme.primary
) {
    Button(
            onClick = onClick,
            modifier = Modifier.width(350.dp).padding(vertical = 5.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) { Text(text) }
}
