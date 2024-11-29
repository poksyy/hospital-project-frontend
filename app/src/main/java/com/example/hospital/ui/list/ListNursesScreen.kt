import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    names: List<String> = listOf("Pau", "Dylan", "Cristian", "Noemi", "Dafne", "Alex", "Jose")
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Enfermeros Registrados",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        LazyColumn {
            items(items = names) { name ->
                NurseList(name = name)
            }
        }
    }
}

@Composable
fun NurseList(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier.padding(8.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}

