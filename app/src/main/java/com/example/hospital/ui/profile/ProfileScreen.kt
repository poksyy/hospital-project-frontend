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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.R
import com.example.hospital.ui.theme.PrimaryColor
import com.example.hospital.ui.factory.ViewModelFactory
import com.example.hospital.ui.utils.rememberBitmapFromByteArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    // Navigates back to the HomeScreen.
    onNavigateBack: () -> Unit,
    // LoginScreen redirection.
    onLogout: () -> Unit,
    // Gets ProfileViewModel with custom factory using the Application context.
    viewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {

    val nurse by viewModel.nurse.collectAsState()
    val profileImage by viewModel.profileImage.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    val updateSuccess by viewModel.updateSuccess.collectAsState()
    val bitmap = rememberBitmapFromByteArray(profileImage)

    // Update success dialog
    if (showUpdateDialog) {
        AlertDialog(onDismissRequest = { showUpdateDialog = false },
            title = { Text("Success") },
            text = { Text("The profile information has been changed successfully") },
            confirmButton = {
                IconButton(onClick = { showUpdateDialog = false }) {
                    Icon(
                        imageVector = Icons.Default.Close, contentDescription = "Close"
                    )
                }
            })
    }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            showUpdateDialog = true
            viewModel.resetUpdateSuccess()
        }
    }

    // Monitor changes to `nurse` and update name and username dynamically.
    var name by remember { mutableStateOf(nurse?.name ?: "") }
    var username by remember { mutableStateOf(nurse?.user ?: "") }

    // Update name and username whenever nurse changes.
    LaunchedEffect(nurse) {
        name = nurse?.name ?: ""
        username = nurse?.user ?: ""
    }

    if (showDeleteDialog) {
        AlertDialog(onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProfile()
                        showDeleteDialog = false
                        onLogout()
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background logo
        Image(
            painter = painterResource(id = R.drawable.hospital_logo),
            contentDescription = null,
            modifier = Modifier
                .size(600.dp)
                .align(Alignment.Center)
                .alpha(0.1f)
        )

        // Main content
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(title = { }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent, navigationIconContentColor = Color.Black
            )
            )

            Column(
                modifier = Modifier
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
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Profile Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Profile",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Name field
                OutlinedTextField(value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.width(280.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black, focusedBorderColor = PrimaryColor
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Name",
                            tint = Color.Black
                        )
                    })

                Spacer(modifier = Modifier.height(16.dp))

                // Username field
                OutlinedTextField(value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.width(280.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black, focusedBorderColor = PrimaryColor
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Username",
                            tint = Color.Black
                        )
                    })

                Spacer(modifier = Modifier.height(32.dp))

                // Save changes button
                Button(
                    onClick = {
                        nurse?.let {
                            // We create a copy of the actual nurse with the new values.
                            val updatedNurse = it.copy(
                                name = name, user = username
                            )
                            viewModel.updateProfile(updatedNurse)
                            showUpdateDialog = true
                        }
                    },
                    modifier = Modifier
                        .width(250.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("SAVE CHANGES", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Change password button
                Button(
                    onClick = { /* TODO: Implement password change */ },
                    modifier = Modifier
                        .width(250.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("CHANGE PASSWORD", fontWeight = FontWeight.Bold)
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
                        text = "DELETE ACCOUNT", color = Color.Red, fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Logout button
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .width(250.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "LOGOUT", color = Color.Black, fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}