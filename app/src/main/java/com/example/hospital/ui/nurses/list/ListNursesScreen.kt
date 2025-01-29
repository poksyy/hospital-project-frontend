package com.example.hospital.ui.nurses.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.R
import com.example.hospital.data.api.Nurse
import com.example.hospital.ui.utils.rememberBitmapFromByteArray

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    val nursesViewModel: NursesListViewModel = viewModel()
    val nurses by nursesViewModel.nurses.collectAsState()
    val isLoading by nursesViewModel.isLoading.collectAsState()
    val error by nursesViewModel.error.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp)) // Space at the top

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Back button
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Flexible space

            // Title
            Text(
                text = "Registered Nurses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f)) // Flexible space

            // Hospital logo
            Image(
                painter = painterResource(id = R.drawable.hospital_logo),
                contentDescription = "Hospital Logo",
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space below the header

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Display loading, error, or content based on state
            when {
                isLoading -> CircularProgressIndicator() // Show loading indicator
                error != null -> Text(
                    text = error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error // Show error message
                )

                nurses.isEmpty() -> Text("No nurses found") // No nurses available
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    items(items = nurses) { nurse ->
                        NurseList(nurse = nurse) // Display each nurse
                    }
                }
            }
        }
    }
}

@Composable
fun NurseList(nurse: Nurse, modifier: Modifier = Modifier) {
    val bitmap = rememberBitmapFromByteArray(nurse.profileImage) // Convert byte array to bitmap

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display nurse's profile image or default icon
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(50.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default Profile",
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp)) // Space between image and text

            Column {
                // Display nurse's name
                Text(
                    text = nurse.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}