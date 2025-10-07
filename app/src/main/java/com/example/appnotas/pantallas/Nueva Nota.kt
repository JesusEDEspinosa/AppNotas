package com.example.appnotas.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaNotaTopBar() {
    LargeTopAppBar(
        title = {
            Text(text = "Nueva Nota")
        }
    )
}

@Composable
fun Nueva() {
    Text(text = "Aquí será la nota")
}

@Preview
@Composable
fun PantallaNuevaNota() {
    Scaffold(
        topBar = {
            NuevaNotaTopBar()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Nueva()
            }
        }
    )
}