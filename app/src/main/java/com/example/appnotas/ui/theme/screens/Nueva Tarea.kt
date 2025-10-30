package com.example.appTareas.ui.theme.screens

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appnotas.ui.theme.NoteViewModel
import com.example.appnotas.ui.theme.NoteViewModelFactory
import com.example.appnotas.ui.theme.newNote.BotonGuardar
import androidx.compose.ui.res.stringResource
import com.example.appnotas.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaTareaTopBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.new_task_title))
        }
    )
}

@Composable
fun NuevaTareaScreen(
    navController: NavController,
    viewModel: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    var textoTitulo by remember { mutableStateOf("") }
    var textoTarea by remember { mutableStateOf("") }

    Scaffold(
        topBar = { NuevaTareaTopBar() },
        floatingActionButton = {
            BotonGuardar(
                onGuardarClick = {
                    viewModel.insertNote(textoTitulo, textoTarea, "Tarea")
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
                    label = { Text(stringResource(R.string.task_title_label)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                TextField(
                    value = textoTarea,
                    onValueChange = { textoTarea = it },
                    label = { Text(stringResource(R.string.what_to_write)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                AgregarRecordatorio()
            }
        }
    )
}

@Composable
fun AgregarRecordatorio(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var estaActivado by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember { mutableStateOf<Date?>(null) }
    val calendario = Calendar.getInstance()
    val formato = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

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
            Text(text = stringResource(R.string.add_reminder), style = MaterialTheme.typography.titleMedium)

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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (fechaSeleccionada != null)
                            "${stringResource(R.string.reminder_prefix)} ${formato.format(fechaSeleccionada!!)}"
                        else
                            stringResource(R.string.select_date_and_time)
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
                                    fechaSeleccionada = calendario.time
                                },
                                calendario.get(Calendar.YEAR),
                                calendario.get(Calendar.MONTH),
                                calendario.get(Calendar.DAY_OF_MONTH)
                            )
                            datePicker.show()
                        }) {
                            Text(stringResource(R.string.select_date))
                        }

                        Button(onClick = {
                            val timePicker = TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    calendario.set(Calendar.HOUR_OF_DAY, hour)
                                    calendario.set(Calendar.MINUTE, minute)
                                    fechaSeleccionada = calendario.time
                                },
                                calendario.get(Calendar.HOUR_OF_DAY),
                                calendario.get(Calendar.MINUTE),
                                true
                            )
                            timePicker.show()
                        }) {
                            Text(stringResource(R.string.select_hour))
                        }
                    }
                }
            }
        }
    }
}