package com.example.appnotas.ui.theme.newNote

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appnotas.ui.theme.NoteViewModel
import com.example.appnotas.ui.theme.NoteViewModelFactory
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesNotaTopBar() {
    TopAppBar(
        title = {
            Text(text = "Editar Nota")
        }
    )
}

@Composable
fun DetallesNotaScreen(
    navController: NavController,
    noteId: Int,
    viewModel: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val note by viewModel.getNoteById(noteId).collectAsState(initial = null)

    var textoTitulo by remember { mutableStateOf("") }
    var textoNota by remember { mutableStateOf("") }

    LaunchedEffect(note) {
        note?.let {
            textoTitulo = it.title
            textoNota = it.content
        }
    }

    if (note == null) {
        Scaffold(
            topBar = { DetallesNotaTopBar() }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    } else {
        Scaffold(
            topBar = { DetallesNotaTopBar() },
            floatingActionButton = {
                BotonGuardarDetalles(
                    onGuardarClick = {
                        viewModel.updateNoteDetails(
                            id = noteId,
                            newTitle = textoTitulo,
                            newContent = textoNota,
                            oldNote = note!!
                        )
                        navController.popBackStack()
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    OutlinedTextField(
                        value = textoTitulo,
                        onValueChange = { textoTitulo = it },
                        label = { Text("Título de la nota") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    TextField(
                        value = textoNota,
                        onValueChange = { textoNota = it },
                        label = { Text("¿Qué deseas escribir?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        )
    }
}



@Composable
fun BotonGuardarDetalles(
    onGuardarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onGuardarClick,
        containerColor = Color.Cyan,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
    ) {
        Icon(Icons.Filled.Done, "Guardar cambios")
    }
}
