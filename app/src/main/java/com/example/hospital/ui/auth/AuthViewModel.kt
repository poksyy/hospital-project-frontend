import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.hospital.data.api.Nurse
import com.example.hospital.data.api.RetrofitInstance

class AuthViewModel : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.None)
    val loginResult: StateFlow<LoginResult> = _loginResult

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun login() {
        if (username.isBlank() || password.isBlank()) {
            _loginResult.value = LoginResult.Failure("Username and password cannot be empty")
            return
        }

        val nurse = Nurse(user = username, password = password)

        viewModelScope.launch {
            _loginResult.value = LoginResult.Loading
            try {
                val response = RetrofitInstance.api.login(nurse)
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.value = LoginResult.Success(response.body()!!)
                } else {
                    _loginResult.value = LoginResult.Failure("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Failure("Error: ${e.message}")
                Log.e("AuthViewModel", "Login error: ${e.message}")
            }
        }
    }

    fun resetLoginState() {
        _loginResult.value = LoginResult.None
    }
}

sealed class LoginResult {
    object None : LoginResult()
    object Loading : LoginResult()
    data class Success(val nurse: Nurse) : LoginResult()
    data class Failure(val error: String) : LoginResult()
}
