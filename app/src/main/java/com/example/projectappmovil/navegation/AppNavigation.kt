package com.example.projectappmovil.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectappmovil.AllReports
import com.example.projectappmovil.Comments
import com.example.projectappmovil.Inicio
import com.example.projectappmovil.CreateReport
import com.example.projectappmovil.Password
import com.example.projectappmovil.Profile
import com.example.projectappmovil.Reports1
import com.example.projectappmovil.previewLogin
import com.example.projectappmovil.registro

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.MainScreen.route) {
        composable(AppScreens.MainScreen.route) {
            previewLogin(navController)
        }
        composable(AppScreens.RegisterScreen.route) {
            registro(navController)
        }
        composable(AppScreens.ForgotScreen.route) {
            Password(navController)
        }
        composable(AppScreens.InicioScreen.route) {
            Inicio(navController)
        }
        composable(AppScreens.CreateReportScreen.route) {
            CreateReport(navController)
        }
        composable(AppScreens.MyReportsScreen.route) {
            Reports1(navController)
        }
        composable(AppScreens.ProfileScreen.route) {
            Profile(navController)
        }
        composable(AppScreens.AllReportsScreen.route) {
            AllReports(navController)
        }
        composable(
            route = AppScreens.CommentsScreen.route,
            arguments = listOf(navArgument("idReport") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val idReport = navBackStackEntry.arguments?.getString("idReport")
            if (idReport != null) {
                Comments(navController, idReport)
            }
        }

        }

}