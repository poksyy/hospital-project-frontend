package com.example.hospital.ui.navigation

import SearchNurseScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.ui.auth.AuthViewModel
import com.example.hospital.ui.auth.LoginScreen
import com.example.hospital.ui.auth.RegisterScreen
import com.example.hospital.ui.home.HomeScreen
import com.example.hospital.ui.nurses.list.ListNursesScreen
import com.example.hospital.ui.profile.ProfileScreen

@Composable
fun HospitalNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    // Starting point of the app (Login screen).
    startDestination: String = NavRoutes.Login.route,
    // Create an instance of AuthViewModel.
    authViewModel: AuthViewModel = viewModel(),
   // homeViewModel: HomeViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Login screen navigation setup.
        composable(NavRoutes.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    // Navigate to Register screen.
                    navController.navigate(NavRoutes.Register.route)
                },
                onLoginSuccess = {
                    // On successful login, navigate to Home screen and pop Login screen.
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Register screen navigation setup.
        composable(NavRoutes.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateBack = {
                    // Go back to the previous screen (Login screen).
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    // On successful registration, navigate back to Login screen and pop Register screen.
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // Home screen navigation setup.
        composable(NavRoutes.Home.route) {
            HomeScreen(
                // homeViewModel = homeViewModel,
                authViewModel = authViewModel,
                onNavigateToNurses = {
                    // Navigate to Search Nurses screen.
                    navController.navigate(NavRoutes.NurseSearch.route)
                },
                onNavigateToListNurses = {
                    // Navigate to List Nurses screen.
                    navController.navigate(NavRoutes.NursesList.route)
                },
                onNavigateToProfile = {
                    // Navigate to Profile screen.
                    navController.navigate(NavRoutes.Profile.route)
                },
                onLogout = {
                    // Log out and navigate back to Login screen.
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Nurses List screen navigation setup.
        composable(NavRoutes.NursesList.route) {
            ListNursesScreen(
                onNavigateBack = {
                    // Go back to the previous screen (Home screen).
                    navController.popBackStack()
                }
            )
        }

        // Nurse Search screen navigation setup.
        composable(NavRoutes.NurseSearch.route) {
            SearchNurseScreen(
                onNavigateBack = {
                    // Go back to the previous screen (Home screen).
                    navController.popBackStack()
                }
            )
        }

        // Profile screen navigation setup.
        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    // Go back to the previous screen (Home screen).
                    navController.popBackStack()
                },
                onLogout = {
                    // Logout, clear session, and navigate back to Login screen.
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
