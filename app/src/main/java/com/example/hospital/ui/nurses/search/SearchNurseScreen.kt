package com.example.hospital.ui.nurses.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hospital.R

@Composable
fun SearchScreen(
    viewModel: SearchNurseViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackPressed: () -> Unit
) {
    val searchText by viewModel.searchText.collectAsState()
    val filteredNurses by viewModel.filteredNurses.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    // Fetch nurses when the screen is loaded
    viewModel.searchNurses()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // Header with Back Button, Title, and Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Search Nurses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            // Hospital logo
            Image(
                painter = painterResource(id = R.drawable.hospital_logo),
                contentDescription = "Hospital Logo",
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar and Information
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Search nurse by name:",
                modifier = Modifier.padding(bottom = 20.dp),
                fontSize = 25.sp
            )

            // Search bar for nurses.
            TextField(
                value = searchText,
                onValueChange = { newText -> viewModel.updateSearchText(newText) },
                label = { Text("Search nurses:") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Information text for how many nurses have been found.
            Text(
                text = "Showing ${filteredNurses.size} nurses",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Blue
            )

            // Show the nurses list below the search bar.
            filteredNurses.forEach { nurse ->
                Text(
                    text = nurse.name,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Show an error message if the nurse is not found
            errorMessage?.let {
                Text(
                    text = it,  // Error message text
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}