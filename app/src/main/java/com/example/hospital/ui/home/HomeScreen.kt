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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HospitalTheme {
                MainScreen(
                    loginViewModel = LoginViewModel()
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    loginViewModel: LoginViewModel
) {
    var showListScreen by remember { mutableStateOf(false) }
    var showSearchScreen by remember { mutableStateOf(false) }
    var showLoginScreen by remember { mutableStateOf(false) }

    val onBackPressed: () -> Unit = {
        if (showListScreen) {
            showListScreen = false
        } else if (showSearchScreen) {
            showSearchScreen = false
        } else if (showLoginScreen) {
            showLoginScreen = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            showListScreen -> {
                ListScreen(onBackPressed = onBackPressed)
            }

            showSearchScreen -> {
                SearchScreen()
            }

            showLoginScreen -> {
                LoginScreen(
                    loginViewModel = loginViewModel,
                    onLoginResult = { isSuccess ->
                        if (isSuccess) {
                            showLoginScreen = false
                            showListScreen = true
                        }
                    }
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hospital_logo),
                        contentDescription = "Hospital Logo",
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

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
    }
}

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
