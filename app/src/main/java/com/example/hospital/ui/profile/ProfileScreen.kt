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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.R
import com.example.hospital.ui.theme.PrimaryColor
import com.example.hospital.ui.factory.ViewModelFactory

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
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    val updateMessage by viewModel.updateMessage.collectAsState()

    // Monitor changes to nurse and update name and username dynamically.
    var name by remember { mutableStateOf(nurse?.name ?: "") }
    var username by remember { mutableStateOf(nurse?.user ?: "") }

    // Update name and username whenever nurse changes.
    LaunchedEffect(nurse) {
        name = nurse?.name ?: ""
        username = nurse?.user ?: ""
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProfile()
                        showDeleteDialog = false
                        onLogout()
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

    if (showPasswordDialog) {
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Change Password") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                            passwordError = null
                        },
                        label = { Text("New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            passwordError = null
                        },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )

                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Password security validations.
                        when {
                            newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                                passwordError = "Password fields cannot be empty"
                            }
                            newPassword.length < 8 -> {
                                passwordError = "Password must be at least 8 characters"
                            }
                            !newPassword.matches(".*[A-Z].*".toRegex()) -> {
                                passwordError = "Password must contain at least one uppercase letter"
                            }
                            !newPassword.matches(".*[0-9].*".toRegex()) -> {
                                passwordError = "Password must contain at least one number"
                            }
                            !newPassword.matches(".*[!@#\$%^&*()\\-_+=<>?].*".toRegex()) -> {
                                passwordError = "Password must contain at least one special character"
                            }
                            newPassword != confirmPassword -> {
                                passwordError = "Passwords do not match"
                            }
                            else -> {
                                viewModel.updatePassword(newPassword)
                                showPasswordDialog = false
                            }
                        }
                    }
                ) {
                    Text("Change")
                }
            },
            dismissButton = {
                Button(onClick = { showPasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
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
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
                    Icon(
                        painter = painterResource(id = R.drawable.hospital_logo),
                        contentDescription = "Hospital Logo",
                        modifier = Modifier.size(60.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Name field
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

                // Username field
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
                            // Check if the values have actually changed
                            if (name != it.name || username != it.user) {
                                // We create a copy of the actual nurse with the new values.
                                val updatedNurse = it.copy(
                                    name = name,
                                    user = username
                                )
                                viewModel.updateProfile(updatedNurse)
                            } else {
                                // We set the boolean at true for the red color message.
                                viewModel.setUpdateMessage("No changes detected", true)
                            }
                        }
                    },
                    modifier = Modifier
                        .width(250.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("UPDATE PROFILE", fontWeight = FontWeight.Bold)
                }

                // Update message
                updateMessage?.let { message ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message.message,
                        color = if (message.isError) Color.Red else Color.Green,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Change password button
                Button(
                    onClick = { showPasswordDialog = true },
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
                        text = "DELETE ACCOUNT",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
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
                        text = "LOGOUT",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}