package com.example.hospital.ui.nurses.search

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class SearchNurseViewModel : ViewModel() {
    // Hardcoded list of nurses.
    private val nurses = listOf("Dylan", "Pau", "Cristian")

    // Holds the search text entered by the user.
    private val _searchText = mutableStateOf("")
    val searchText: State<String> = _searchText

    // Filters the nurse list based on the search text.
    private val _filteredNurses = mutableStateOf(nurses)
    val filteredNurses: State<List<String>> = _filteredNurses

    // Show all nurses if the search text is empty; otherwise, filter the list based on the search text (case-insensitive).
    fun updateSearchText(newText: String) {
            _searchText.value = newText

            if (newText.isEmpty()) {
                _filteredNurses.value = nurses
            } else {
                _filteredNurses.value = nurses.filter { it.lowercase().contains(newText.lowercase()) }
            }
        }
    }
