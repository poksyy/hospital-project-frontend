package com.example.hospital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.hospital.ui.navigation.HospitalNavHost
import com.example.hospital.ui.theme.HospitalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HospitalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Initialize NavController to manage navigation between screens.
                    val navController = rememberNavController()

                    // Set up the NavHost with the navController, which handles all screen navigation.
                    HospitalNavHost(navController = navController)
                }
            }
        }
    }
}