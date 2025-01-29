package com.example.hospital.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Icon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.zIndex
import com.example.hospital.R
import com.example.hospital.ui.theme.PrimaryColor
import com.example.hospital.ui.factory.ViewModelFactory

// Main profile screen composable that handles user profile display and editing.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    ),
    onBackPressed: () -> Unit
) {
    // State management for profile data and UI.
    val nurse by viewModel.nurse.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Remember the name and username and defaulting and empty String if null.
    var name by remember(nurse?.name) { mutableStateOf(nurse?.name ?: "") }
    var username by remember(nurse?.user) { mutableStateOf(nurse?.user ?: "") }

    // Delete confirmation dialog.
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProfile()

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Background logo
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.hospital_logo),
            contentDescription = null,
            modifier = Modifier
                .size(600.dp)
                .align(Alignment.Center)
                .alpha(0.1f),
            contentScale = ContentScale.Crop
        )
    }

    // Main content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
    ) {
        // Top navigation bar
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = Color.Black
            )
        )

        // Profile content
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Profile picture
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.hospital_logo),
                    contentDescription = "Hospital Logo",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Editable name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.width(280.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = PrimaryColor
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Name",
                        tint = Color.Black
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Editable username field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.width(280.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = PrimaryColor
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Username",
                        tint = Color.Black
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Save changes button
            Button(
                onClick = {
                    nurse?.let {
                        viewModel.updateProfile(it.copy(name = name, user = username))
                    }
                },
                modifier = Modifier
                    .width(250.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "SAVE CHANGES",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Change password button
            Button(
                onClick = { /* TODO: Implement password change */ },
                modifier = Modifier
                    .width(250.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "CHANGE PASSWORD",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Delete account button
            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .width(250.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "DELETE ACCOUNT",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout button
            Button(
                onClick = onBackPressed,
                modifier = Modifier
                    .width(250.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "LOGOUT",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
