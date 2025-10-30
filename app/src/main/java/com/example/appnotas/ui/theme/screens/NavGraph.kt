package com.example.appnotas.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appTareas.ui.theme.screens.NuevaTareaScreen
import com.example.appnotas.ui.theme.newNote.DetallesNotaScreen
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
        composable(
            route = "detalles_nota/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
            DetallesNotaScreen(navController = navController, noteId = noteId)
        }
    }
}
