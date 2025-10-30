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
import androidx.compose.ui.res.stringResource
import com.example.appnotas.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaNotaTopBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.new_note_title))
        }
    )
}

@Composable
fun NuevaNotaScreen(
    navController: NavController,
    viewModel: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    var textoTitulo by remember { mutableStateOf("") }
    var textoNota by remember { mutableStateOf("") }

    Scaffold(
        topBar = { NuevaNotaTopBar() },
        floatingActionButton = {
            BotonGuardar(
                onGuardarClick = {
                    viewModel.insertNote(textoTitulo, textoNota, "Nota")
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
                    label = { Text(stringResource(R.string.note_title_label)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                TextField(
                    value = textoNota,
                    onValueChange = { textoNota = it },
                    label = { Text(stringResource(R.string.what_to_write)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    )
}



@Composable
fun BotonGuardar(
    onGuardarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onGuardarClick,
        containerColor = Color.Cyan,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
    ) {
        Icon(Icons.Filled.Done, stringResource(R.string.add_new_note_description))
    }
}