package com.example.hospital.ui.factory

import SearchNurseViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hospital.ui.auth.AuthViewModel
import com.example.hospital.ui.profile.ProfileViewModel
import com.example.hospital.ui.nurses.list.NursesListViewModel
import com.example.hospital.image.ImageViewModel

// This ViewModelFactory is used to create instances of ViewModel classes.
// It helps provide the necessary dependencies (e.g., Application) when constructing ViewModels.
class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    // Create a map of ViewModel classes to their respective instances.
    private val viewModels: Map<Class<out ViewModel>, () -> ViewModel> = mapOf(
        AuthViewModel::class.java to { AuthViewModel(application) },
        ProfileViewModel::class.java to { ProfileViewModel(application) },
        NursesListViewModel::class.java to { NursesListViewModel() },
        ImageViewModel::class.java to { ImageViewModel() },
        SearchNurseViewModel::class.java to { SearchNurseViewModel() }
    )

    // Create and return the appropriate ViewModel instance based on the requested class type.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModels[modelClass]?.invoke() as? T
            ?: throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
