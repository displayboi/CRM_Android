package com.example.crm_bueno2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crm_bueno2.screens.AgendaPage
import com.example.crm_bueno2.screens.CalendarPage
import com.example.crm_bueno2.screens.HomePage
import com.example.crm_bueno2.screens.LoginPage
import com.example.crm_bueno2.screens.MessagesPage
import com.example.crm_bueno2.screens.RegisterPage
import com.example.crm_bueno2.screens.SettingsPage

@Composable
fun NavGraph( startDestination: String = "login",onThemeChange: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("login") { LoginPage(navController) }
        composable("register") { RegisterPage(navController) }
        composable("home") { HomePage(navController) }
        composable("agenda") { AgendaPage(navController) }
        composable("calendar") { CalendarPage(navController) }
        composable("messages") { MessagesPage(navController) }
        composable("settings") { SettingsPage(onThemeChange) }

    }
}
