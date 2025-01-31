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
fun ListNursesScreen(
    // Navigates back to the HomeScreen.
    onNavigateBack: () -> Unit,
    // Allows data export from NursesListViewModel.
    viewModel: NursesListViewModel = viewModel()
) {
    // Collecting state from the ViewModel: nurses list, loading state, and errors.
    val nurses by viewModel.nurses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with back button, title, and logo.
        Header(onNavigateBack)

        // Display loading indicator, error message, or the list of nurses based on states
        if (isLoading) {
            LoadingIndicator()
        }

        if (error != null) {
            ErrorMessage(error)
        }

        if (!isLoading && error == null) {
            NursesList(nurses) // Displays the list of nurses if there’s no error or loading
        }
    }
}

// Displays the header with back button, title, and hospital logo
@Composable
private fun Header(onNavigateBack: () -> Unit) {
    Spacer(modifier = Modifier.height(64.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Registered Nurses",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.hospital_logo),
            contentDescription = "Hospital Logo",
            modifier = Modifier.size(50.dp)
        )
    }
}

// Loading indicator shown when data is being fetched.
@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

// Error message displayed if there's an error.
@Composable
private fun ErrorMessage(error: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = error ?: "Unknown error", color = MaterialTheme.colorScheme.error)
    }
}

// Displays the list of nurses or a message if the list is empty.
@Composable
private fun NursesList(nurses: List<Nurse>) {
    if (nurses.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No nurses found")
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(items = nurses) { nurse ->
                NurseListItem(nurse = nurse)
            }
        }
    }
}

// Displays individual nurse's details in a card format.
@Composable
private fun NurseListItem(nurse: Nurse, modifier: Modifier = Modifier) {
    val bitmap = rememberBitmapFromByteArray(nurse.profileImage)

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
            // Display the nurse's profile image or a default icon if null bitMap.
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

            Spacer(modifier = Modifier.width(16.dp))

            // Nurse's name displayed in bold text.
            Column {
                Text(
                    text = nurse.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
