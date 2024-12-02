package com.example.hospital.ui.auth
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hospital.R

@Composable
fun LoginScreen(onLoginResult: (Boolean) -> Unit) {
    // Variables to store the user input for username and password
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    // Correct credentials (hardcoded)
    val correctUsername = "admin"
    val correctPassword = "123"


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        // Title with image
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cross),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Welcome to the Nurse Login",
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
            )
        }


        Spacer(modifier = Modifier.height(24.dp))


        // User field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))


        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))


        // Log in button with custom background and text color
        Button(
            onClick = {
                if (username == correctUsername && password == correctPassword) {
                    onLoginResult(true)
                } else {
                    onLoginResult(false)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.Red, contentColor = Color.White
            )
        ) {
            Text("Log In")
        }
    }
}
