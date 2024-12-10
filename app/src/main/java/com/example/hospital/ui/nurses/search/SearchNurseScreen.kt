package com.example.hospital.ui.nurses.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
// Observe the search text and filtered nurse list from the SearchNurseViewModel.
fun SearchScreen(viewModel: SearchNurseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    // Observe  the state of the text.
    val searchText by viewModel.searchText
    // Observe the state of the filtered list.
    val filteredNurses by viewModel.filteredNurses

    // Column for the inputs structure.
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Search nurse by name:",
            modifier = Modifier.padding(bottom = 20.dp),
            fontSize = 25.sp
        )

        // Search bar for nurses.
        TextField(value = searchText,
            onValueChange = { newText -> viewModel.updateSearchText(newText) },
            label = { Text("Search nurses:") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        // Information text for how many nurses has been found.
        Text(
            text = "Showing ${filteredNurses.size} nurses",
            modifier = Modifier.padding(top = 8.dp),
            color = Color.Blue
        )

        // Show the nurses list below the search bar.
        filteredNurses.forEach { nurse ->
            Text(
                text = nurse, modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
