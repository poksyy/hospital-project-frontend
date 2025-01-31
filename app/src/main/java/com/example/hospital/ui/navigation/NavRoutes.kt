package com.example.hospital.ui.navigation

sealed class NavRoutes(val route: String) {
    data object Login : NavRoutes("hospital/login")
    data object Register : NavRoutes("hospital/register")
    data object Home : NavRoutes("hospital/home")
    data object NursesList : NavRoutes("hospital/nurses_list")
    data object NurseSearch : NavRoutes("hospital/nurse_search")
    data object Profile : NavRoutes("hospital/profile")
}
