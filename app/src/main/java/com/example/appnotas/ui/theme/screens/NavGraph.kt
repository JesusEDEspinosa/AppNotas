package com.example.appnotas.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appTareas.ui.theme.screens.NuevaTareaScreen
import com.example.appnotas.ui.theme.newNote.NuevaNotaScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "menu"
    ) {
        composable("menu") {
            MenuScreen(navController)
        }
        composable("nueva_nota") {
            NuevaNotaScreen(navController)
        }
        composable("nueva_tarea") {
            NuevaTareaScreen(navController)
        }
    }
}
