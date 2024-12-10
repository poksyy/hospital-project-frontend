import androidx.lifecycle.ViewModel
import com.example.hospital.R
import com.example.hospital.ui.nurses.list.Nurse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NursesListViewModel : ViewModel() {
    private val _nurses = MutableStateFlow<List<Nurse>>(
        listOf(
            Nurse("Pau", "Carrera", R.drawable.nurse1, "Senior Nurse"),
            Nurse("Cristian", "Oraña", R.drawable.nurse2, "Junior Nurse"),
            Nurse("Dylan", "Navarro", R.drawable.nurse3, "Head Nurse"),
            Nurse("Alex", "Rodríguez", R.drawable.nurse4, "Junior Nurse"),
            Nurse("Dafne Michelle", "Ramírez", R.drawable.nurse5, "Junior Nurse"),
            Nurse("Noemí", "Saladie", R.drawable.nurse6, "Senior Nurse")
        )
    )
    val nurses: StateFlow<List<Nurse>> get() = _nurses
}
