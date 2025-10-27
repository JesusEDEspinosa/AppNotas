package com.example.appnotas.ui.theme.newNote

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
fun Nueva(
    modifier: Modifier = Modifier
) {
    var textoNota by remember { mutableStateOf("") }

    TextField(
        value = textoNota,
        onValueChange = { },
        label = { Text("¿Que deseas escribir?") },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun Titulo(
    modifier: Modifier = Modifier
) {
    var textoTitulo by remember { mutableStateOf("") }

    OutlinedTextField(
        value = textoTitulo,
        onValueChange = { },
        label = { Text("Título de la nota") },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Preview(showBackground = true)
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
                    .padding(paddingValues)
            ) {
                Titulo()

                Nueva()

                AgregarRecordatorio()
            }
        }
    )
}

@Composable
fun AgregarRecordatorio(
    modifier: Modifier = Modifier
) {
    var estaActivado by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Espacio entre elementos
        ) {
            Text(text = "Agregar Recordatorio", style = MaterialTheme.typography.titleMedium)

            Switch(
                checked = estaActivado,
                onCheckedChange = { nuevoValor ->
                    estaActivado = nuevoValor
                }
            )
        }

        if (estaActivado) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = "Configuración de fecha y hora del recordatorio (Contenido que se activa)",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}