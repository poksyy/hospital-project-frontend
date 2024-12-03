
import com.example.hospital.ui.nurses.search.SearchScreen
import com.example.hospital.ui.nurses.list.ListScreen
import com.example.hospital.ui.auth.LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.hospital.ui.theme.HospitalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HospitalTheme {
                MainScreen()
            }
        }
    }
}

// MainScreen: Controls navigation between different screens.
@Composable
fun MainScreen() {
    // Mutable states to track which screen is currently displayed
    var showListScreen by remember { mutableStateOf(false) }  // Show the list screen
    var showSearchScreen by remember { mutableStateOf(false) } // Show the search screen
    var showLoginScreen by remember { mutableStateOf(false) }  // Show the login screen

    // Use a `when` expression to decide which screen to show.
    when {
        showListScreen -> {
            // Display the ListScreen component.
            ListScreen()
        }

        showSearchScreen -> {
            // Display the SearchScreen component.
            SearchScreen()
        }

        showLoginScreen -> {
            // Display the LoginScreen component with a callback for login results.
            LoginScreen(onLoginResult = { isSuccess ->
                if (isSuccess) {
                    // If login is successful, hide the login screen and show the list screen.
                    showLoginScreen = false
                    showListScreen = true
                }
            })
        }

        else -> {
            // Default: Display the landing page with buttons for navigation.
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Row containing navigation buttons.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Button to navigate to the SearchScreen.
                    Button(onClick = {
                        showSearchScreen = true
                    }) {
                        Text("Search Nurses")
                    }
                    // Button to navigate to the ListScreen.
                    Button(onClick = {
                        showListScreen = true
                    }) {
                        Text("List of Nurses")
                    }
                    // Button to navigate to the LoginScreen.
                    Button(onClick = {
                        showLoginScreen = true
                    }) {
                        Text("Login")
                    }
                }
            }
        }
    }
}
