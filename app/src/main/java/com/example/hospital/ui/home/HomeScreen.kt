package com.example.hospital.ui.home

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
import com.example.hospital.R
import com.example.hospital.ui.auth.AuthViewModel

@Composable
fun HomeScreen(
    // SearchNurseScreen redirection.
    onNavigateToNurses: () -> Unit,
    // ProfileScreen redirection.
    onNavigateToProfile: () -> Unit,
    // ListNurseScreen redirection.
    onNavigateToListNurses: () -> Unit,
    // LoginScreen redirection.
    onLogout: () -> Unit,
    // Allows authentication-related data export from AuthViewModel.
    authViewModel: AuthViewModel = viewModel()
) {
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
            text = "Manage your hospital tasks with ease",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            // Triggers the ListNurseScreen redirection.
            onClick = onNavigateToListNurses,
            modifier = Modifier
                .width(350.dp)
                .padding(vertical = 5.dp),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text("List Nurses")
        }

        Button(
            // Triggers the SearchNurseScreen redirection.
            onClick = onNavigateToNurses,
            modifier = Modifier
                .width(350.dp)
                .padding(vertical = 5.dp),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text("Search Nurses")
        }

        Button(
            // Triggers the ProfileScreen redirection.
            onClick = onNavigateToProfile,
            modifier = Modifier
                .width(350.dp)
                .padding(vertical = 5.dp),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text("My Profile")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Triggers the logout logic and redirection to LoginScreen.
                authViewModel.logout()
                onLogout()
            },
            modifier = Modifier
                .width(350.dp)
                .padding(vertical = 5.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB71C1C)
            )
        ) {
            Text("Logout")
        }
    }
}