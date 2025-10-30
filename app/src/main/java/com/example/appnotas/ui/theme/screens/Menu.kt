package com.example.appnotas.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appnotas.data.local.Note
import com.example.appnotas.ui.theme.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.stringResource
import com.example.appnotas.R
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavController, noteViewModel: NoteViewModel = viewModel()) {
    var menuAbierto by remember { mutableStateOf(false) }

    val notes by noteViewModel.filteredNotes.collectAsState()

    val searchQuery by noteViewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.notes)) })
        },
        floatingActionButton = {
            Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.BottomEnd) {
                Column(horizontalAlignment = Alignment.End) {
                    AnimatedVisibility(
                        visible = menuAbierto,
                        enter = fadeIn(animationSpec = tween(200)) + expandVertically(),
                        exit = fadeOut(animationSpec = tween(200)) + shrinkVertically()
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            ExtendedFloatingActionButton(
                                text = { Text(stringResource(R.string.note)) },
                                icon = { Icon(Icons.Filled.NoteAdd, contentDescription = null) },
                                onClick = {
                                    navController.navigate("nueva_nota")
                                    menuAbierto = false
                                },
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            ExtendedFloatingActionButton(
                                text = { Text(stringResource(R.string.task)) },
                                icon = { Icon(Icons.Filled.Assignment, contentDescription = null) },
                                onClick = {
                                    navController.navigate("nueva_tarea")
                                    menuAbierto = false
                                },
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    FloatingActionButton(
                        onClick = { menuAbierto = !menuAbierto },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_description))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { noteViewModel.onSearchQueryChange(it) }, // Conecta al ViewModel
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (searchQuery.isBlank()) stringResource(R.string.no_notes_yet)
                        else stringResource(R.string.no_results_found),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(notes) { note ->
                        NoteItem(
                            note = note,
                            onNoteClick = {
                                navController.navigate("detalles_nota/${note.id}")
                            },
                            onToggleComplete = {
                                noteViewModel.taskCompletion(note)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onNoteClick: () -> Unit,
    onToggleComplete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    val textColor = if (note.completa) Color.Gray else Color.DarkGray
    val textDecoration = if (note.completa) TextDecoration.LineThrough else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { onNoteClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme. typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (note.completa) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    textDecoration = textDecoration
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.content.take(80) + if (note.content.length > 80) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    textDecoration = textDecoration
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = dateFormat.format(Date(note.dateCreated)),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    if (note.tipo == "Nota") {
                        Text(
                            text = stringResource(R.string.note_type_label),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1E88E5)
                        )
                    }
                }
            }
            if (note.tipo == "Tarea") {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    Checkbox(
                        checked = note.completa,
                        onCheckedChange = { onToggleComplete() }
                    )
                    Text(
                        text = stringResource(R.string.task_type_label),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF1E88E5)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .height(50.dp),
        placeholder = { Text(stringResource(R.string.search_placeholder), style = MaterialTheme.typography.bodySmall, color = Color.Gray) },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.search_description), tint = Color.DarkGray)
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),

        )
}