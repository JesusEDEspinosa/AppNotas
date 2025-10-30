package com.example.appnotas.ui.theme.newNote

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Delete
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesNotaTopBar(tipo: String, onDeleteClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = if (tipo == "Tarea") "Editar Tarea" else "Editar Nota")
        },
        actions = {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar nota"
                )
            }
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

    var textoTitulo by remember(note?.id) { mutableStateOf(note?.title ?: "") }
    var textoNota by remember(note?.id) { mutableStateOf(note?.content ?: "") }
    var recordatorio by remember(note?.id) { mutableStateOf(note?.recordatorio) }

    var mensajeBorrar by remember { mutableStateOf(false) }

    if (mensajeBorrar) {
        AlertDialog(
            onDismissRequest = { mensajeBorrar = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estas seguro de eliminar esta nota?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mensajeBorrar = false
                        note?.let { viewModel.deleteNote(it) }
                        navController.popBackStack()
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mensajeBorrar = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (note == null) {
        Scaffold(
            topBar = { DetallesNotaTopBar(tipo = "Nota", onDeleteClick = {}) }
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
            topBar = {
                DetallesNotaTopBar(
                    tipo = note!!.tipo,
                    onDeleteClick = {
                        mensajeBorrar = true
                    }

                ) },
            floatingActionButton = {
                BotonGuardarDetalles(
                    onGuardarClick = {
                        viewModel.updateNote(
                            id = noteId,
                            newTitle = textoTitulo,
                            newContent = textoNota,
                            newRecordatorio = recordatorio,
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
                        label = { Text("Título") },
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
                    if (note!!.tipo == "Tarea") {
                        AgregarRecordatorio(
                            recordatorio = recordatorio,
                            onRecordatorioChange = { nuevoRecordatorio ->
                                recordatorio = nuevoRecordatorio
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun AgregarRecordatorio(
    recordatorio: Long?,
    onRecordatorioChange: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val estaActivado = recordatorio != null
    val calendario = remember { Calendar.getInstance() }
    val formato = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }


    LaunchedEffect(recordatorio) {
        if (recordatorio != null) {
            calendario.timeInMillis = recordatorio
        } else {
            calendario.timeInMillis = System.currentTimeMillis()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Agregar Recordatorio", style = MaterialTheme.typography.titleMedium)

            Switch(
                checked = estaActivado,
                onCheckedChange = { nuevoValor ->
                    if (nuevoValor) {
                        onRecordatorioChange(calendario.time.time)
                    } else {
                        onRecordatorioChange(null)
                    }
                }
            )
        }

        if (estaActivado && recordatorio != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Recordatorio: ${formato.format(Date(recordatorio))}"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            val datePicker = DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    calendario.set(Calendar.YEAR, year)
                                    calendario.set(Calendar.MONTH, month)
                                    calendario.set(Calendar.DAY_OF_MONTH, day)
                                    onRecordatorioChange(calendario.time.time)
                                },
                                calendario.get(Calendar.YEAR),
                                calendario.get(Calendar.MONTH),
                                calendario.get(Calendar.DAY_OF_MONTH)
                            )
                            datePicker.show()
                        }) {
                            Text("Cambiar Fecha")
                        }

                        Button(onClick = {
                            val timePicker = TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    calendario.set(Calendar.HOUR_OF_DAY, hour)
                                    calendario.set(Calendar.MINUTE, minute)
                                    onRecordatorioChange(calendario.time.time)
                                },
                                calendario.get(Calendar.HOUR_OF_DAY),
                                calendario.get(Calendar.MINUTE),
                                true
                            )
                            timePicker.show()
                        }) {
                            Text("Cambiar Hora")
                        }
                    }
                }
            }
        }
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
