<<<<<<< Updated upstream:app/src/main/java/com/example/hospital/ui/search/SearchNurseScreen.kt
=======
package com.example.hospital.ui.nurses.search
>>>>>>> Stashed changes:app/src/main/java/com/example/hospital/ui/nurses/search/SearchNurseScreen.kt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchScreen() {
    // Holds the search input text.
    var searchText by remember { mutableStateOf("") }

    // Nurses test list.
    val nurses = listOf("NurseTest1", "NurseTest2", "NurseTest3")

    // Filters the nurse list based on the search text, case-insensitive.
    val filteredNurses = nurses.filter {
        it.lowercase().contains(searchText.lowercase())
    }

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
            onValueChange = { newText -> searchText = newText },
            label = { Text("Search nurses:") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text(
            text = "Showing ${filteredNurses.size} nurses",
            modifier = Modifier.padding(top = 8.dp),
            color = Color.Blue
        )

        // Show the nurses list below the search bar.
        filteredNurses.forEach { nurse ->
            Text(
                text = nurse,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
