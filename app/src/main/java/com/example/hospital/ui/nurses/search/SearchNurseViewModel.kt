import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchNurseViewModel : ViewModel() {

    // Holds the search query text.
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    // Stores the filtered list of nurses based on the search.
    private val _filteredNurses = MutableStateFlow<List<Nurse>>(emptyList())
    val filteredNurses: StateFlow<List<Nurse>> = _filteredNurses

    // Indicates when data is being loaded.
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Holds any error messages encountered during the process.
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Automatically fetches all nurses on initialization (when invoked on the SearchNurseScreen).
    init {
        searchNurses()
    }

    // This is called whenever the search text changes in the UI.
    // It updates the search query state and triggers a new nurse search.
    fun updateSearchText(newText: String) {
        _searchText.value = newText
        searchNurses()
    }

    // Executes the search based on the search text, either by name or fetching all nurses.
    private fun searchNurses() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = if (_searchText.value.isBlank()) {
                    getAllNurses()
                } else {
                    searchNursesByName(_searchText.value)
                }

                result.fold(
                    onSuccess = { nurses ->
                        _filteredNurses.value = nurses
                        _error.value = null
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Unknown error"
                        _filteredNurses.value = emptyList()
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _filteredNurses.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Searches nurses by name through the API.
    private suspend fun searchNursesByName(name: String): Result<List<Nurse>> {
        return try {
            val response = RetrofitInstance.api.getNurseByName(name)
            if (response.isSuccessful) {
                response.body()?.let { nurse ->
                    Result.success(listOf(nurse))
                } ?: Result.success(emptyList())
            } else {
                Result.failure(Exception("No nurses found with that name"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fetches all nurses from the API.
    private suspend fun getAllNurses(): Result<List<Nurse>> {
        return try {
            val response = RetrofitInstance.api.getAllNurses()
            if (response.isSuccessful) {
                response.body()?.let { nurses ->
                    Result.success(nurses)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to fetch nurses"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}