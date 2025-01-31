import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.R

@Composable
fun SearchNurseScreen(
    // Navigates back to the HomeScreen.
    onNavigateBack: () -> Unit,
    // Allows data export from SearchNurseViewModel.
    viewModel: SearchNurseViewModel = viewModel()
) {
    // Collecting state from the ViewModel: nurses list, loading state, and errors.
    val searchText by viewModel.searchText.collectAsState()
    val filteredNurses by viewModel.filteredNurses.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Navigates back to the HomeScreen.
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

            Image(
                painter = painterResource(id = R.drawable.hospital_logo),
                contentDescription = "Hospital Logo",
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Search nurse by name:",
                modifier = Modifier.padding(bottom = 20.dp),
                fontSize = 25.sp
            )

            // Calls updateSearchText() on each text change to update the search bar.
            TextField(
                value = searchText,
                // Updates search text and triggers a new search.
                onValueChange = { viewModel.updateSearchText(it) },
                label = { Text("Search nurses:") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = "Showing ${filteredNurses.size} nurses",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Blue
            )

            filteredNurses.forEach { nurse ->
                Text(
                    text = nurse.name,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}