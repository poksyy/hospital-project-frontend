package com.example.hospital.ui.factory


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hospital.ui.auth.AuthViewModel
import com.example.hospital.ui.profile.ProfileViewModel

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(application) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
                ProfileViewModel(application) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}